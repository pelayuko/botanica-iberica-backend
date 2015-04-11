package org.pelayo.controller;

import java.util.List;

import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZonasController {

	@Autowired
	private TaxonesRepository repo;

	@RequestMapping("/zonasBySector")
	public List<String> zonasBySector(@RequestParam(value = "sector", required = true) String sector) {
		return repo.getNombreZonasBySector(sector);
	}

}
