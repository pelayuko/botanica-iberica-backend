package org.pelayo.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.DatosDeFamiliaResponse;
import org.pelayo.controller.model.TaxonResponse;
import org.pelayo.dao.DatosDeFamiliaRepository;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeFamiliaController {

	private static final Logger log = Logger.getLogger(DatosDeFamiliaController.class);

	@Autowired
	private DatosDeFamiliaRepository repo;

	@RequestMapping("/datosDeFamilia")
	public DatosDeFamiliaResponse datosDeFamilia(@RequestParam(value = "family", required = true) String familia) {
		DatosDeFamiliaResponse resp = new DatosDeFamiliaResponse();

		resp.setFamilia(familia);
		List<TaxonResponse> species = repo.getTaxonesByFamilia(familia);
		resp.setEspecies(species);

		resp.setFotos(repo.getListFotos(familia, 6));
		resp.setRefFloraIberica(repo.getRefFlora(familia));
		resp.setGrupo(repo.getGrupoFam(familia));

		return resp;
	}

}
