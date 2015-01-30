package org.pelayo.controller;

import org.pelayo.storage.Foto;
import org.pelayo.storage.FotoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FotosPaisajeController {

	@Autowired
	private FotoManager fotoManager;

	@RequestMapping("/randomPhotoLandscape")
	public Foto randomFotoPaisaje() {
		return fotoManager.getRandomFotoPaisaje();
	}

	@RequestMapping("/randomPhotoFlower")
	public Foto randomFotoFlora() {
		return fotoManager.getRandomFotoFlora();
	}
}
