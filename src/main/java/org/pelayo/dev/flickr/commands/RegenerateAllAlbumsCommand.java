package org.pelayo.dev.flickr.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.dao.SectorRepository;
import org.pelayo.dev.flickr.commands.base.AbstractAuthorizedBaseCommand;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.VoidModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.pelayo.model.FotoLugar;
import org.pelayo.model.FotoPlanta;
import org.pelayo.model.Sector;
import org.springframework.context.ApplicationContext;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;

public class RegenerateAllAlbumsCommand extends AbstractAuthorizedBaseCommand<Boolean, VoidModel> {

	private static final Logger log = Logger.getLogger(RegenerateAllAlbumsCommand.class);

	private ApplicationContext ctx;

	public RegenerateAllAlbumsCommand(ApplicationContext ctx, AuthorizedCommandModel model) throws Exception {
		super(model);
		this.ctx = ctx;
	}

	@Override
	public Boolean execute(VoidModel model) throws FlickrException {
		List<Sector> sectores = ctx.getBean(SectorRepository.class).findAll();
		Set<String> nombresAlbums = new HashSet<String>();
		for (Sector sector : sectores) {
			nombresAlbums.add(FlickrHelper.PAISAJE_PREFIX + " " + sector.getDenom());
			nombresAlbums.add(FlickrHelper.PLANTA_PREFIX + " " + sector.getDenom());
		}

		Map<String, String> nombresAlbumsFlickr = new HashMap<String, String>();
		Photosets photosets = flickr.getPhotosetsInterface().getList(userId);
		for (Photoset ps : photosets.getPhotosets()) {
			if (nombresAlbumsFlickr.containsKey(ps.getTitle())) {
				log.info("Duplicated album name " + ps.getTitle());
				PhotoList<Photo> photoList = flickr.getPhotosetsInterface().getPhotos(ps.getId(), 5000, 0);
				for (Photo photo : photoList) {
					try {
						flickr.getPhotosetsInterface().addPhoto(nombresAlbumsFlickr.get(ps.getTitle()), photo.getId());
					} catch (FlickrException e) {
						if (!e.getMessage().contains("3: Photo already in set")) {
							throw new RuntimeException(e);
						}
					}

					flickr.getPhotosetsInterface().removePhoto(ps.getId(), photo.getId());
				}
				try {
					flickr.getPhotosetsInterface().delete(ps.getId());
				} catch (FlickrException e) {
					if (!e.getMessage().contains("1: Photoset not found")) {
						throw new RuntimeException(e);
					}
				}
				log.info("Moved photos");
			} else {
				nombresAlbumsFlickr.put(ps.getTitle(), ps.getId());
			}

		}

		int j = 0;
		int k = 0;
		for (Photoset ps : photosets.getPhotosets()) {
			boolean morePhotos = true;
			int page = 0;
			while (morePhotos) {
				PhotoList<Photo> photoList;
				try {
					photoList = flickr.getPhotosetsInterface().getPhotos(ps.getId(), 500, page);
				} catch (FlickrException e1) {
					log.error("Album not found " + ps.getTitle());

					break;
				}
				morePhotos = photoList.size() == 500;
				page++;
				log.info(++k + " Processing album " + ps.getTitle() + " with " + photoList.size() + " page " + page);
				int i = 0;
				for (Photo photo : photoList) {
					// log.info("Next photo");
					String nombreAlbumTeorico = extractAlbumName(photo);
					i++;
					if (nombreAlbumTeorico == null) {
						log.info(++j + " Theoric name is null");
						continue;
					}

					if (ps.getTitle().equals(nombreAlbumTeorico)) {
						continue;
					}

					log.info(i + " Different " + ps.getTitle() + " - " + nombreAlbumTeorico);
					String psId = nombresAlbumsFlickr.get(nombreAlbumTeorico);
					if (psId == null) {
						psId = flickr.getPhotosetsInterface().create(nombreAlbumTeorico, null, photo.getId()).getId();
						nombresAlbumsFlickr.put(nombreAlbumTeorico, psId);
					}

					try {
						// log.info("Adding photo");
						flickr.getPhotosetsInterface().addPhoto(psId, photo.getId());
					} catch (FlickrException e) {
						if (!e.getMessage().contains("3: Photo already in set")) {
							throw new RuntimeException(e);
						}
					} finally {
						// log.info("Added photo");
					}
					try {
						// log.info("Removing photo");
						flickr.getPhotosetsInterface().removePhoto(ps.getId(), photo.getId());
					} catch (FlickrException e) {
						if (!e.getMessage().contains("3: Photo not in set")) {
							throw new RuntimeException(e);
						}
					} finally {
						// log.info("Removed photo");
					}
				}
			}
		}

		return true;
	}

	private String extractAlbumName(Photo photo) {
		String nombreAlbumTeorico = null;
		FotoLugar fotoLugar = ctx.getBean(FotosLugaresRepository.class).findByFlickrId(photo.getId());
		if (fotoLugar == null) {
			FotoPlanta fotoPlanta = ctx.getBean(FotosPlantasRepository.class).findByFlickrId(photo.getId());
			if (fotoPlanta == null) {
				return null;
			}
			nombreAlbumTeorico = FlickrHelper.PLANTA_PREFIX + " " + fotoPlanta.getSectorName();
		} else {
			nombreAlbumTeorico = FlickrHelper.PAISAJE_PREFIX + " " + fotoLugar.getSectorName();
		}

		return nombreAlbumTeorico;
	}

}
