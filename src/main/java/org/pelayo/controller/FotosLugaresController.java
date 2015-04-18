package org.pelayo.controller;

import java.net.MalformedURLException;
import java.net.URL;

import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.model.FotoLugar;
import org.pelayo.util.FlickrUtil.PhotoSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FotosLugaresController extends BaseFotosController<FotoLugar> {

	@Autowired
	private FotosLugaresRepository fotosLugaresRepository;

	@RequestMapping("/randomPhotoLandscape")
	public URL randomFotoPaisaje(@RequestParam(value = "size", defaultValue = "") PhotoSize size)
			throws MalformedURLException {
		return randomPhoto(fotosLugaresRepository, size);
	}
}
