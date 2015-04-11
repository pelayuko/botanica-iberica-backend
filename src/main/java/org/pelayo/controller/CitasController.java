package org.pelayo.controller;

import java.util.List;

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

	@RequestMapping("/citasByLugar")
	public Iterable<Cita> citasByLugar(@RequestParam(value = "lugar", defaultValue = "") String lugar) {
		return citasRepository.findByLugar(lugar);
	}

	@RequestMapping("/citasByLugar2")
	public Iterable<Cita> citasByLugar2(@RequestParam(value = "lugar", defaultValue = "") String lugar) {
		return citasRepository.findByNombreZona(lugar);
	}

	@RequestMapping("/citasBySector")
	public Iterable<Cita> citasBySector(@RequestParam(value = "sector", required = true) String sector) {
		List<Cita> findBySector = citasRepository.findBySectorEtiq(sector);
		return findBySector;
	}
}
