package org.pelayo.dev.flickr.commands;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.dao.FotosRepository;
import org.pelayo.dev.flickr.commands.base.AbstractUploadCommand;
import org.pelayo.dev.flickr.config.FlickrProps;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.PhotoUploadModel;
import org.pelayo.dev.flickr.model.VoidModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.pelayo.model.FicherosFoto;
import org.pelayo.model.IFoto;
import org.pelayo.storage.config.FotoFloraConfiguration;
import org.pelayo.storage.config.FotoPaisajesConfiguration;
import org.springframework.context.ApplicationContext;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;

public class ProcessAllPhotosCommand extends AbstractUploadCommand<Integer, VoidModel> {

	public enum Mode {
		TAG, UPLOAD, RENAME
	}

	private static final Logger log = Logger.getLogger(ProcessAllPhotosCommand.class);

	private static final int NUMBER_EXECUTORS = 9;

	private static final AtomicInteger sequence = new AtomicInteger(0);

	private static final AtomicInteger sequenceErrors = new AtomicInteger(0);

	private static final AtomicInteger sequenceMissingFile = new AtomicInteger(0);

	private Mode mode;

	public ProcessAllPhotosCommand(ApplicationContext ctx, AuthorizedCommandModel model, FlickrProps props, Mode mode)
			throws Exception {
		super(ctx, model, props);
		this.mode = mode;
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
			log.info("FOTOS TO ITERATE " + fotos.size());
			for (T foto : fotos) {
				FicherosFoto ficheroFoto = foto.getFicherosfoto();
				if (ficheroFoto == null) {
					log.info(sequenceMissingFile.incrementAndGet() + " Error setting tags: " + foto.getFichero());
					continue;
				}

				if (Mode.UPLOAD.equals(mode)) {
					if (FlickrHelper.SUCCESS.equals(ficheroFoto.getFlickrStatus())) {
						log.info(ficheroFoto.getPath() + " is already uploaded, SKIPPING");
						continue;
					}
				}

				String fullPath = basePath + "/" + ficheroFoto.getPath();
				PhotoUploadModel model = createPhotoUploadModel(foto, fullPath);

				log.info(sequence.incrementAndGet() + " " + model.getName());

				switch (mode) {
				case RENAME:
					executeSafeRename(model, ficheroFoto);
					break;
				case TAG:
					executeSafeRetag(model, ficheroFoto);
					break;
				case UPLOAD:
					Photo photo = executeSafeUpload(model, ficheroFoto);
					if (photo != null) {
						log.trace("\tPhoto static url "
								+ String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", photo.getFarm(),
										photo.getServer(), photo.getId(), photo.getSecret()));
					} else {
						log.info(sequenceErrors.incrementAndGet() + " Error uploading: " + model.getFileName());
					}
					break;
				default:
					throw new RuntimeException("Unsupported mode " + mode);
				}
			}
		}

	}

	private void executeSafeRename(PhotoUploadModel model, FicherosFoto ficheroFoto) {
		try {
			flickr.getPhotosInterface().setMeta(ficheroFoto.getFlickrId(), model.getName(), model.getDescription());
		} catch (FlickrException e) {
			log.info(sequenceErrors.incrementAndGet() + " Error renaming: " + model.getFileName());
		}
	}

	private void executeSafeRetag(PhotoUploadModel model, FicherosFoto ficheroFoto) {
		try {
			flickr.getPhotosInterface().setTags(ficheroFoto.getFlickrId(),
					model.getTags().toArray(new String[model.getTags().size()]));
		} catch (FlickrException e) {
			log.info(sequenceErrors.incrementAndGet() + " Error setting tags: " + model.getFileName());
		}
	}

}
