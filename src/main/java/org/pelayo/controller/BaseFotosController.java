package org.pelayo.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.pelayo.dao.FotosRepository;
import org.pelayo.model.IFoto;
import org.pelayo.util.FlickrUtil;
import org.pelayo.util.FlickrUtil.PhotoSize;
import org.springframework.data.domain.PageRequest;

public class BaseFotosController<T extends IFoto> {

	protected URL randomPhoto(FotosRepository<T> repo, PhotoSize size) throws MalformedURLException {
		List<String> randomUrl = repo.findRandomUrl(new PageRequest(0, 1));
		String url = randomUrl.get(0);

		return FlickrUtil.buildUrl(size, url);
	}

}
