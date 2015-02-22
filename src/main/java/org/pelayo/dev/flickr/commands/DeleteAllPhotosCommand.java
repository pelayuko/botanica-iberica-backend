package org.pelayo.dev.flickr.commands;

import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.dao.FicherosFotoRepository;
import org.pelayo.dev.flickr.commands.base.AbstractAuthorizedBaseCommand;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.VoidModel;
import org.pelayo.model.FicherosFoto;
import org.springframework.context.ApplicationContext;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;

public class DeleteAllPhotosCommand extends AbstractAuthorizedBaseCommand<Integer, VoidModel> {

	private static final Logger log = Logger.getLogger(DeleteAllPhotosCommand.class);

	private ApplicationContext ctx;

	public DeleteAllPhotosCommand(ApplicationContext ctx, AuthorizedCommandModel model) throws Exception {
		super(model);
		this.ctx = ctx;
	}

	@Override
	public Integer execute(VoidModel model) throws FlickrException {
		int count = 0;
		Photosets photosets = flickr.getPhotosetsInterface().getList(userId);

		for (Photoset ps : photosets.getPhotosets()) {
			List<Photo> photos = flickr.getPhotosetsInterface().getPhotos(ps.getId(), 10000, 0);
			int ind = 0;
			for (Photo photo : photos) {
				log.info(++ind + " Deleting photo with id " + photo.getId());

				try {
					flickr.getGeoInterface().removeLocation(photo.getId());
				} catch (Exception e) {
					// ignore
				}

				FicherosFotoRepository ficherosFotoRepository = ctx.getBean(FicherosFotoRepository.class);
				List<FicherosFoto> ffs = ficherosFotoRepository.findByFlickrId(photo.getId());
				for (FicherosFoto ff : ffs) {
					ff.setFlickrId(null);
					ff.setFlickrUrl(null);
					ff.setFlickrStatus(null);
					ficherosFotoRepository.save(ff);
				}

				flickr.getPhotosInterface().delete(photo.getId());
				count++;
			}

			flickr.getPhotosetsInterface().delete(ps.getId());
		}

		return count;
	}
}
