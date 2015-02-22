package org.pelayo.dev.flickr.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.pelayo.dao.FicherosFotoRepository;
import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.dao.FotosRepository;
import org.pelayo.dev.flickr.commands.base.AbstractAuthorizedBaseCommand;
import org.pelayo.dev.flickr.config.FlickrProps;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.GeoDataModel;
import org.pelayo.dev.flickr.model.PhotoUploadModel;
import org.pelayo.dev.flickr.model.VoidModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.pelayo.dev.flickr.util.MyStringUtils;
import org.pelayo.model.Cita;
import org.pelayo.model.FicherosFoto;
import org.pelayo.model.FotoLugar;
import org.pelayo.model.FotoPlanta;
import org.pelayo.model.IFoto;
import org.pelayo.storage.config.FotoFloraConfiguration;
import org.pelayo.storage.config.FotoPaisajesConfiguration;
import org.springframework.context.ApplicationContext;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;

public class ProcessAllPhotosCommand extends AbstractAuthorizedBaseCommand<Integer, VoidModel> {

	public enum Mode {
		TAG, UPLOAD, RENAME
	}

	private static final Logger log = Logger.getLogger(ProcessAllPhotosCommand.class);

	private static final int NUMBER_EXECUTORS = 6;

	private static final String ERROR = "ERROR";

	private static final String SUCCESS = "SUCCESS";

	private static final AtomicInteger sequence = new AtomicInteger(0);

	private static final AtomicInteger sequenceErrors = new AtomicInteger(0);

	private static final AtomicInteger sequenceMissingFile = new AtomicInteger(0);

	private ApplicationContext ctx;

	private Mode mode;

	private FlickrProps props;

	public ProcessAllPhotosCommand(ApplicationContext ctx, AuthorizedCommandModel model, FlickrProps props, Mode mode)
			throws Exception {
		super(model);
		this.ctx = ctx;
		this.mode = mode;
		this.props = props;
	}

	@Override
	public Integer execute(VoidModel model) throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(NUMBER_EXECUTORS);

		{
			FotoPaisajesConfiguration fotoPaisajeConfiguration = ctx.getBean(FotoPaisajesConfiguration.class);

			executeRunnable(ctx.getBean(FotosLugaresRepository.class), pool, fotoPaisajeConfiguration.getPath(),
					NUMBER_EXECUTORS / 2);
		}
		{
			FotoFloraConfiguration fotoFloraConfiguration = ctx.getBean(FotoFloraConfiguration.class);

			executeRunnable(ctx.getBean(FotosPlantasRepository.class), pool, fotoFloraConfiguration.getPath(),
					NUMBER_EXECUTORS / 2);
		}

		// pool.awaitTermination(12, TimeUnit.HOURS);
		pool.shutdown();
		while (!pool.isTerminated()) {
		}

		log.error("FINISHED WITH SUCCESS " + sequence.get());

		log.error("FINISHED WITH ERRORS " + sequenceErrors.get());
		log.error("FINISHED WITH MISSING FILES " + sequenceMissingFile.get());

		return sequence.get();
	}

	private <T extends IFoto> void executeRunnable(FotosRepository<T> repository, ExecutorService pool, String path,
			int numberExecutors) throws FlickrException, IOException, SAXException {

		List<T> fotos;
		switch (mode) {
		case UPLOAD:
			fotos = repository.findNotUploadedToFickr();
			break;
		default:
			fotos = repository.findAll();
			break;
		}
		log.info("Photos to be procesed " + fotos.size());

		boolean lessSizeThanExecutors = fotos.size() < numberExecutors;
		int sliceSize = lessSizeThanExecutors ? fotos.size() : fotos.size() / (numberExecutors - 1);
		int startIndex;
		int endIndex;
		for (int i = 0; i < (lessSizeThanExecutors ? 1 : numberExecutors); i++) {
			startIndex = sliceSize * i;
			endIndex = sliceSize * (i + 1);
			if (endIndex > fotos.size()) {
				endIndex = fotos.size();
			}
			pool.execute(new UploadFotosRunnable<T>(fotos.subList(startIndex, endIndex), path));
		}
	}

	class UploadFotosRunnable<T extends IFoto> implements Runnable {
		private List<T> fotos;
		private String basePath;

		public UploadFotosRunnable(List<T> fotos, String basePath) throws FlickrException, IOException, SAXException {
			this.fotos = fotos;
			this.basePath = basePath;
		}

		@Override
		public void run() {
			UploadPhotoCommand upPhoto = newUploadPhotoCommand();
			log.info("FOTOS TO ITERATE " + fotos.size());
			for (T foto : fotos) {
				FicherosFoto ficheroFoto = foto.getFicherosfoto();
				if (ficheroFoto == null) {
					log.info(sequenceMissingFile.incrementAndGet() + " Error setting tags: " + foto.getFichero());
					continue;
				}

				if (Mode.UPLOAD.equals(mode)) {
					if (SUCCESS.equals(ficheroFoto.getFlickrStatus())) {
						log.info(ficheroFoto.getPath() + " is already uploaded, SKIPPING");
						continue;
					}
				}

				String fullPath = basePath + "/" + ficheroFoto.getPath();
				PhotoUploadModel model = createPhotoUploadModel(foto, fullPath);

				log.info(sequence.incrementAndGet() + " " + model.getName());

				switch (mode) {
				case RENAME:
					executeSafeRename(upPhoto, model, ficheroFoto);
					break;
				case TAG:
					executeSafeRetag(upPhoto, model, ficheroFoto);
					break;
				case UPLOAD:
					Photo photo = executeSafeUpload(upPhoto, model, ficheroFoto);
					if (photo != null) {
						log.trace("\tPhoto static url "
								+ String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", photo.getFarm(),
										photo.getServer(), photo.getId(), photo.getSecret()));
					}
					break;
				default:
					throw new RuntimeException("Unsupported mode " + mode);
				}
			}
		}

		private Flickr newFlickr() {
			return new Flickr(props.getKey(), props.getSecret(), new REST());
		}

		private UploadPhotoCommand newUploadPhotoCommand() {

			try {
				return new UploadPhotoCommand(AuthorizedCommandModel.mk().withFlickr(newFlickr())
						.withUsername(props.getUsername()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void executeSafeRename(UploadPhotoCommand upPhoto, PhotoUploadModel model, FicherosFoto ficheroFoto) {
		try {
			upPhoto.getFlickr().getPhotosInterface()
					.setMeta(ficheroFoto.getFlickrId(), model.getName(), model.getDescription());
		} catch (FlickrException e) {
			log.info(sequenceErrors.incrementAndGet() + " Error renaming: " + model.getFileName());
		}
	}

	private void executeSafeRetag(UploadPhotoCommand upPhoto, PhotoUploadModel model, FicherosFoto ficheroFoto) {
		try {
			upPhoto.getFlickr().getPhotosInterface()
					.setTags(ficheroFoto.getFlickrId(), model.getTags().toArray(new String[model.getTags().size()]));
			log.info(sequence.getAndIncrement() + " Tags set by " + Thread.currentThread().getId());
		} catch (FlickrException e) {
			log.info(sequenceErrors.incrementAndGet() + " Error setting tags: " + model.getFileName());
		}
	}

	private Photo executeSafeUpload(UploadPhotoCommand upPhoto, PhotoUploadModel model, FicherosFoto ficheroFoto) {
		Photo photo = null;
		FicherosFotoRepository fichFotosRepository = ctx.getBean(FicherosFotoRepository.class);
		try {
			File f = new File(model.getFileName());
			if (!f.exists()) {
				throw new IllegalArgumentException("File not exists " + f.getName());
			}
			if (f.isDirectory()) {
				throw new IllegalArgumentException("Not supported for directories " + f.getName());
			}
			photo = upPhoto.execute(model);

			ficheroFoto.setFlickrId(photo.getId());
			ficheroFoto.setFlickrUrl(String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", photo.getFarm(),
					photo.getServer(), photo.getId(), photo.getSecret()));
			ficheroFoto.setFlickrStatus(SUCCESS);
			fichFotosRepository.save(ficheroFoto);
		} catch (Exception e) {
			log.info(sequenceErrors.incrementAndGet() + " Error uploading: " + model.getFileName());
			ficheroFoto.setFlickrStatus(ERROR);
			fichFotosRepository.save(ficheroFoto);
		}
		return photo;

	}

	private PhotoUploadModel createPhotoUploadModel(IFoto foto, String fullPath) {
		// FIXME: implement visitor pattern!
		if (foto instanceof FotoPlanta) {
			return _createPhotoUploadModel((FotoPlanta) foto, fullPath);
		}
		if (foto instanceof FotoLugar) {
			return _createPhotoUploadModel((FotoLugar) foto, fullPath);
		}
		throw new RuntimeException("Unsupported class " + foto.getFichero());
	}

	private PhotoUploadModel _createPhotoUploadModel(FotoLugar fotoLugar, String fullPath) {
		return PhotoUploadModel.mk()
				.withFileName(fullPath)
				//
				.withAlbumName(FlickrHelper.PAISAJE_PREFIX + " " + fotoLugar.getSectorName())
				//
				.withName(fotoLugar.getComentario())
				//
				.withDescription(
						fotoLugar.getZona().getNombre() + "\n"
								+ MyStringUtils.voidIfNull(fotoLugar.getZona().getDescripción())) //
				.withGeoData(GeoDataModel.fromUTM(fotoLugar.getCoord())) //
				.withTag(FlickrHelper.PAISAJE_PREFIX) //
				.withTag(fotoLugar.getZona().getNombre()) //
				.withTag(fotoLugar.getSectorName()) //
				.withTag("'Flora Ibérica'");
	}

	private PhotoUploadModel _createPhotoUploadModel(FotoPlanta fotoPlanta, String fullPath) {
		Cita cita = fotoPlanta.getCita();
		String name = cita.getEspecie().getNomespec().getNombreGen() + " "
				+ MyStringUtils.voidIfNull(cita.getEspecie().getNomespec().getRestrictEsp()) + " "
				+ MyStringUtils.voidIfNull(cita.getEspecie().getNomespec().getRestrSubesp());
		return PhotoUploadModel.mk().withFileName(fullPath)
				.withAlbumName(FlickrHelper.PLANTA_PREFIX + " " + fotoPlanta.getSectorName())
				//
				.withName(name)
				//
				.withDescription(fotoPlanta.getComentario() + "\n" + MyStringUtils.voidIfNull(cita.getComentario())) //
				.withGeoData(GeoDataModel.fromUTM(cita.getCoord())) //
				.withTag(FlickrHelper.PLANTA_PREFIX) //
				.withTag(cita.getEspecie().getColor()) //
				.withTag(cita.getEspecie().getGenero().getNombreGen()) //
				.withTag(cita.getEspecie().getNomespec().getNombreGen()) //
				.withTag(cita.getZona().getNombre()) //
				.withTag(fotoPlanta.getSectorName()) //

				.withTag(cita.getEspecie().getGenero().getNombreGen()) //
				.withTag(cita.getEspecie().getGenero().getNomComun()) //
				.withTag(cita.getEspecie().getGenero().getFamilia().getNombreFam()) //
				.withTag(name) //
				.withTag("'Flora Ibérica'");
	}

}
