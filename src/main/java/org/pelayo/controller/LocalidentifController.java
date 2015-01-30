package org.pelayo.controller;

import org.pelayo.dao.LocalidentifRepository;
import org.pelayo.model.Localidentif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocalidentifController {

	@Autowired
	private LocalidentifRepository localidentifRepository;

	@RequestMapping("/allLocalidentif")
	public Page<Localidentif> allLocalidentif(Pageable pageable) {
		Page<Localidentif> citas = localidentifRepository.findAll(pageable);
		return citas;
	}

	@RequestMapping("/Localidentif")
	public Iterable<Localidentif> getLocalidentifByLugar(
			@RequestParam(value = "lugar", defaultValue = "") String lugar) {
		return localidentifRepository.findByLugar(lugar);
	}

	@RequestMapping("/Localidentif2")
	public Iterable<Localidentif> getLocalidentifByLugar2(
			@RequestParam(value = "lugar", defaultValue = "") String lugar) {
		return localidentifRepository.findByLugar2(lugar);
	}

}
