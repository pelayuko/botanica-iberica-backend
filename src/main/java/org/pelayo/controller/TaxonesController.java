package org.pelayo.controller;

import java.util.List;

import org.pelayo.controller.model.TaxonResponse;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaxonesController {

	@Autowired
	private TaxonesRepository repo;

	@RequestMapping("/taxonesByNombreAceptado")
	public List<TaxonResponse> taxonesByNombre(@RequestParam(value = "nombre", required = true) String nombre) {
		return repo.taxonesByNombre(nombre);
	}

	@RequestMapping("/taxonesByNombreComun")
	public List<TaxonResponse> taxonesByNombreComun(@RequestParam(value = "nombre", required = true) String nombre) {
		return repo.taxonesByNombreComun(nombre);
	}

	@RequestMapping("/listaTaxonesByGenero")
	public List<TaxonResponse> listaTaxonesByGenero(@RequestParam(value = "genero", required = true) String genero) {
		return repo.getTaxonesByGenero(genero);
	}
	
	@RequestMapping("/listaTaxonesByFamilia")
	public List<TaxonResponse> listaTaxonesByFamilia(@RequestParam(value = "familia", required = true) String familia) {
		return repo.getTaxonesByFamilia(familia);
	}

}
