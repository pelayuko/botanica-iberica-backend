package org.pelayo.dev.flickr.commands.base;

import java.io.File;

import org.apache.log4j.Logger;
import org.pelayo.dao.FicherosFotoRepository;
import org.pelayo.dev.flickr.config.FlickrProps;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.GeoDataModel;
import org.pelayo.dev.flickr.model.IModel;
import org.pelayo.dev.flickr.model.PhotoUploadModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.pelayo.dev.flickr.util.MyStringUtils;
import org.pelayo.model.Cita;
import org.pelayo.model.FicherosFoto;
import org.pelayo.model.FotoLugar;
import org.pelayo.model.FotoPlanta;
import org.pelayo.model.IFoto;
import org.springframework.context.ApplicationContext;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;

public abstract class AbstractUploadCommand<T, Y extends IModel> extends AbstractAuthorizedBaseCommand<T, Y> {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(AbstractUploadCommand.class);

	protected ApplicationContext ctx;

	private FlickrProps props;

	public AbstractUploadCommand(ApplicationContext ctx, AuthorizedCommandModel model, FlickrProps props)
			throws Exception {
		super(model);
		this.ctx = ctx;
		this.props = props;
	}

	protected Photo executeSafeUpload(PhotoUploadModel model, FicherosFoto ficheroFoto) {

		Photo photo = null;
		FicherosFotoRepository fichFotosRepository = ctx.getBean(FicherosFotoRepository.class);
		try {
			PhotoUploader upPhoto = new PhotoUploader(AuthorizedCommandModel.mk()
					.withFlickr(new Flickr(props.getKey(), props.getSecret(), new REST()))
					.withUsername(props.getUsername()));

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
			ficheroFoto.setFlickrStatus(FlickrHelper.SUCCESS);
			fichFotosRepository.save(ficheroFoto);
		} catch (Exception e) {
			ficheroFoto.setFlickrStatus(FlickrHelper.ERROR);
			fichFotosRepository.save(ficheroFoto);
		}
		return photo;

	}

	protected PhotoUploadModel createPhotoUploadModel(IFoto foto, String fullPath) {
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
				.withTag("Aragón") //
				.withTag("Spain") //
				.withTag("Flora Ibérica");
	}

	private PhotoUploadModel _createPhotoUploadModel(FotoPlanta fotoPlanta, String fullPath) {
		Cita cita = fotoPlanta.getCita();
		String name = cita.getEspecie().getNomespec().getNombreGen() + " "
				+ MyStringUtils.voidIfNull(cita.getEspecie().getNomespec().getRestrictEsp()) + " "
				+ MyStringUtils.voidIfNull(cita.getEspecie().getNomespec().getRestrSubesp());

		String nombreFitoTipo = null;
		if (cita.getEspecie().getFitoTipo() != null) {
			nombreFitoTipo = cita.getEspecie().getFitoTipo().getTipo();
		}

		return PhotoUploadModel.mk().withFileName(fullPath)
				.withAlbumName(FlickrHelper.PLANTA_PREFIX + " " + fotoPlanta.getSectorName())
				//
				.withName(name)
				//
				.withDescription(MyStringUtils.voidIfNull(fotoPlanta.getComentario()) + "\n" + MyStringUtils.voidIfNull(cita.getComentario())) //
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
				.withTag(nombreFitoTipo) //
				.withTag("Aragón") //
				.withTag("Spain") //
				.withTag("Flora Ibérica");
	}

}
