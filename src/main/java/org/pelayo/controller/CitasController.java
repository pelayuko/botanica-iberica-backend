package org.pelayo.controller;

import org.pelayo.dao.CitasRepository;
import org.pelayo.model.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CitasController {

	@Autowired
	private CitasRepository citasRepository;

	@RequestMapping("/citas")
	public Page<Cita> citas(Pageable pageable) {
		Page<Cita> citas = citasRepository.findAll(pageable);
		return citas;
	}

	@RequestMapping("/citaByLugar")
	public Iterable<Cita> getCitafByLugar(
			@RequestParam(value = "lugar", defaultValue = "") String lugar) {
		return citasRepository.findByLugar(lugar);
	}

	@RequestMapping("/citaByLugar2")
	public Iterable<Cita> getCitafByLugar2(
			@RequestParam(value = "lugar", defaultValue = "") String lugar) {
		return citasRepository.findByLugar2(lugar);
	}

}
