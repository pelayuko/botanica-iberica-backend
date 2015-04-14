package org.pelayo.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.CitaResponse;
import org.pelayo.dao.ListaCitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ListaCitasController {

	private static final Logger log = Logger.getLogger(ListaCitasController.class);

	@Autowired
	private ListaCitasRepository repo;

	@RequestMapping("/listaCitasByUtmsTaxon")
	public List<CitaResponse> listaCitasByUtmsTaxon(@RequestParam(value = "taxon", required = true) String taxon) {
		return repo.listaCitasByUtmsTaxon(taxon);
	}
	
	@RequestMapping("/listaCitasJacaByUtmsTaxon")
	public List<CitaResponse> listaCitasJacaByUtmsTaxon(@RequestParam(value = "taxon", required = true) String taxon) {
		return repo.listaCitasJacaByUtmsTaxon(taxon);
	}

	@RequestMapping("/listaCitasByUtmsZona")
	public List<CitaResponse> listaCitasByUtmsZona(@RequestParam(value = "zona", required = true) String zona) {
		return repo.listaCitasByUtmsZona(zona);
	}

	@RequestMapping("/listaCitasByUtmsSector")
	public List<CitaResponse> listaCitasByUtmsSector(@RequestParam(value = "sector", required = true) String sector) {
		return repo.listaCitasByUtmsSector(sector);
	}

}
