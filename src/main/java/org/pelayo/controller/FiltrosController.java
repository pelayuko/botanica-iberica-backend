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
	public String filtroByFamilia(@RequestParam(value = "familia", required = true) String familia) {
		if (Especie.filtro.isEmpty() || Especie.filtro.startsWith(" and G")) Especie.filtro = " and Familia = '"+ familia + "'";
		else Especie.filtro = "";
		return Especie.filtro;
	}
	
	@RequestMapping("/filtroByGenero")
	public String filtroByGenero(@RequestParam(value = "genero", required = true) String genero) {
		if (Especie.filtro.isEmpty() || Especie.filtro.startsWith(" and F")) Especie.filtro = " and Género = '"+ genero + "'";
		else Especie.filtro = "";
		return Especie.filtro;
	}

}
