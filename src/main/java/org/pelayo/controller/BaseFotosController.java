package org.pelayo.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.pelayo.dao.FotosRepository;
import org.pelayo.model.IFoto;
import org.springframework.data.domain.PageRequest;

public class BaseFotosController<T extends IFoto> {

	final static String DEFAULT_SIZE = PhotoSize.MEDIUM.toString();

	/*
	 * 
	 * 
	 * s small square 75x75 q large square 150x150 t thumbnail, 100 on longest
	 * side m small, 240 on longest side n small, 320 on longest side - medium,
	 * 500 on longest side z medium 640, 640 on longest side c medium 800, 800
	 * on longest side† b large, 1024 on longest side* h large 1600, 1600 on
	 * longest side† k large 2048, 2048 on longest side† o original image,
	 * either a jpg, gif or png, depending on source format
	 */
	public enum PhotoSize {
		SMALL_SQUARE, THUMBNAIL, MEDIUM
	}

	protected URL randomPhoto(FotosRepository<T> repo, PhotoSize size) throws MalformedURLException {
		List<String> randomUrl = repo.findRandomUrl(new PageRequest(0, 1));
		String url = randomUrl.get(0);

		String base = url.substring(0, url.lastIndexOf("."));

		if (size != null) {
			switch (size) {
			case SMALL_SQUARE:
				base += "_s";
				break;
			case THUMBNAIL:
				base += "_t";
				break;
			case MEDIUM:
				base += "";
				break;
			default:
				break;
			}
		}

		return new URL(base + url.substring(url.lastIndexOf(".")));
	}
}
