package org.pelayo.controller;

import org.pelayo.dao.TaxonesRepository;
import org.pelayo.model.Especie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FiltrosController {
	
	@Autowired
	private TaxonesRepository repo;

	@RequestMapping("/filtroByFamilia")
	public String zonasBySector(@RequestParam(value = "familia", required = true) String familia) {
		if (Especie.filtro.isEmpty()) Especie.filtro = " and Familia = '"+ familia + "'";
		else Especie.filtro = "";
		return Especie.filtro;
	}

}
