package org.pelayo.dev.flickr.commands;

import java.io.File;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.pelayo.dao.FicherosFotoRepository;
import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.dev.flickr.commands.base.AbstractUploadCommand;
import org.pelayo.dev.flickr.config.FlickrProps;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.PhotoUploadModel;
import org.pelayo.dev.flickr.model.SimpleUploadPhotoModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.pelayo.model.FicherosFoto;
import org.pelayo.model.IFoto;
import org.pelayo.storage.config.FotoFloraConfiguration;
import org.pelayo.storage.config.FotoPaisajesConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flickr4java.flickr.photos.Photo;

public class SimpleUploadPhotoCommand extends AbstractUploadCommand<Boolean, SimpleUploadPhotoModel> {

	private static final Logger log = Logger.getLogger(SimpleUploadPhotoCommand.class);

	public SimpleUploadPhotoCommand(ApplicationContext ctx, AuthorizedCommandModel model, FlickrProps props)
			throws Exception {
		super(ctx, model, props);
	}

	@Override
	public Boolean execute(SimpleUploadPhotoModel model) throws Exception {

		switch (model.getMode()) {
		case FLOWER:
			createFoto(ctx.getBean(FotosPlantasRepository.class), ctx.getBean(FotoFloraConfiguration.class).getPath(),
					model);
			break;
		case LANDSCAPE:
			createFoto(ctx.getBean(FotosLugaresRepository.class), ctx.getBean(FotoPaisajesConfiguration.class)
					.getPath(), model);
			break;
		default:
			break;

		}

		return Boolean.TRUE;
	}

	public <T extends IFoto> void createFoto(JpaRepository<T, Integer> repo, String basePath,
			SimpleUploadPhotoModel model) {
		T foto = repo.findOne(Integer.parseInt(model.getPhotoId()));
		if (foto == null) {
			throw new RuntimeException("Photo with id " + model.getPhotoId() + " not found!");
		}

		String path = foto.getSectorName() + "/" + foto.getFichero() + ".jpg";

		String fullPath = basePath + "/" + path;
		File f = new File(fullPath);
		if (!f.exists()) {
			throw new RuntimeException("File does no exist!! " + fullPath);
		}

		FicherosFoto ficheroFoto = foto.getFicherosfoto();
		if (ficheroFoto == null) {
			log.info("Ficheros Foto no existe, creando " + fullPath);

			ficheroFoto = createFicheroFoto(ctx, repo, foto, path);
		}

		if (FlickrHelper.SUCCESS.equals(ficheroFoto.getFlickrStatus())) {
			log.info(ficheroFoto.getPath() + " is already uploaded!!, SKIPPING");
			return;
		}

		PhotoUploadModel uhotoUploadModel = createPhotoUploadModel(foto, fullPath);
		Photo photo = executeSafeUpload(uhotoUploadModel, ficheroFoto);
		if (photo == null) {
			throw new RuntimeException("Error uploading photo!! " + uhotoUploadModel);
		}

	}

	public <T extends IFoto> FicherosFoto createFicheroFoto(ApplicationContext ctx, JpaRepository<T, Integer> repo,
			T sectorProvider, String path) {
		FicherosFoto ficheroFoto = new FicherosFoto();
		ficheroFoto.setId(UUID.randomUUID().toString());
		ficheroFoto.setPath(path);
		// ficheroFoto.addFotoslugare(fotoLugar);
		ctx.getBean(FicherosFotoRepository.class).save(ficheroFoto);

		sectorProvider.setFicherosfoto(ficheroFoto);
		repo.save(sectorProvider);

		return ficheroFoto;
	}
}
