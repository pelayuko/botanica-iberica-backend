package org.pelayo.controller;

import java.net.MalformedURLException;
import java.net.URL;

import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.model.FotoPlanta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FotosPlantasController extends BaseFotosController<FotoPlanta> {

	@Autowired
	private FotosPlantasRepository fotosPlantasRepository;

	@RequestMapping("/randomPhotoFlower")
	public URL randomFotoFlora(@RequestParam(value = "size", defaultValue = "") PhotoSize size)
			throws MalformedURLException {
		return randomPhoto(fotosPlantasRepository, size);
	}
}
