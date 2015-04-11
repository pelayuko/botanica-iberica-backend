package org.pelayo.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.apache.log4j.Logger;
import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.dev.flickr.commands.base.AbstractAuthorizedBaseCommand;
import org.pelayo.dev.flickr.config.FlickrProps;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.VoidModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.pelayo.model.FotoPlanta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

@RestController
public class FotosPlantasController extends BaseFotosController<FotoPlanta> {

	private static final Logger log = Logger.getLogger(FotosPlantasController.class);

	@Autowired
	private FotosPlantasRepository fotosPlantasRepository;

	@Autowired
	private FlickrProps flickrProps;

	private Flickr flickr;

	private Random randomGenerator;

	public FotosPlantasController() {
		randomGenerator = new Random();
	}

	@RequestMapping("/randomPhotoFlower")
	public URL randomFotoFlora(@RequestParam(value = "size", defaultValue = "") PhotoSize size)
			throws MalformedURLException {
		return randomPhoto(fotosPlantasRepository, size);
	}

	@RequestMapping("/randomPhotoFlowerByTag")
	public URL randomFotoFloraByTag(@RequestParam(value = "tag", defaultValue = "") final String tag,
			@RequestParam(value = "size", defaultValue = "") PhotoSize size) throws Exception {

		log.info("Searching for tag = " + tag);

		flickr = new Flickr(flickrProps.getKey(), flickrProps.getSecret(), new REST());

		AuthorizedCommandModel model = AuthorizedCommandModel.mk().withFlickr(flickr)
				.withUsername(flickrProps.getUsername());
		String result = new AbstractAuthorizedBaseCommand<String, VoidModel>(model) {

			@Override
			public String execute(VoidModel model) throws Exception {
				PhotosInterface iface = getFlickr().getPhotosInterface();
				SearchParameters searchParameters = new SearchParameters();
				String[] tags = { "flower", tag };
				searchParameters.setTags(tags);
				// searchParameters.setUserId(userId);
				PhotoList<Photo> photos = iface.search(searchParameters, 100, 0);

				if (photos.isEmpty()) {
					return null;
				}

				int index = randomGenerator.nextInt(photos.size());

				log.info("FOUND! returning index " + index);

				return FlickrHelper.buildUrl(photos.get(index));
			}

		}.execute(VoidModel.mk());

		if (result == null)
			return randomFotoFlora(size);

		return buildUrl(size, result);
	}
}
