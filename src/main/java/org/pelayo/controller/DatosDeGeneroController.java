package org.pelayo.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.DatosDeGeneroResponse;
import org.pelayo.controller.model.TaxonResponse;
import org.pelayo.dao.DatosDeGeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeGeneroController {

	private static final Logger log = Logger.getLogger(DatosDeGeneroController.class);

	@Autowired
	private DatosDeGeneroRepository repo;

	@RequestMapping("/datosDeGenero")
	public DatosDeGeneroResponse datosDeGenero(@RequestParam(value = "genus", required = true) String genus) {
		DatosDeGeneroResponse resp = new DatosDeGeneroResponse();

		resp.setGenero(genus);
		List<TaxonResponse> species = repo.getTaxonesByGenero(genus);
		resp.setEspecies(species);

		if (!species.isEmpty()) {
			resp.setFamilia(species.get(0).getFamilia());
		}

		resp.setFotos(repo.getListFotos(genus, 6));
		
		resp.setRefFloraIberica(repo.getRefFlora(genus));
		
		return resp;
	}

}
