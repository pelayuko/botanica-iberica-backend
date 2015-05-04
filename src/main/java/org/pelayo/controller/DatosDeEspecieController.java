package org.pelayo.controller;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.DatosDeEspecieResponse;
import org.pelayo.dao.DatosDeEspecieRepository;
import org.pelayo.model.Especie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeEspecieController {
	
	private static final Logger log = Logger.getLogger(DatosDeEspecieController.class);

	@Autowired
	private DatosDeEspecieRepository repo;

	@RequestMapping("/datosDeEspecieRandom")
	public DatosDeEspecieResponse datosDeEspecieRandom() {
//		Especie.filtro="";
		return getDatosEspecie(repo.taxonAlAzar());
	}

	@RequestMapping("/datosDeEspecieNext")
	public DatosDeEspecieResponse datosDeEspecieNext(@RequestParam(value = "ident", required = true) String ident) {
		return getDatosEspecie(repo.relativeTaxon(ident, false));
	}

	@RequestMapping("/datosDeEspeciePrev")
	public DatosDeEspecieResponse datosDeEspeciePrev(@RequestParam(value = "ident", required = true) String ident) {
		return getDatosEspecie(repo.relativeTaxon(ident, true));
	}

	@RequestMapping("/datosDeEspecie")
	public DatosDeEspecieResponse datosDeEspecie(@RequestParam(value = "ident", required = true) String ident) {
		Especie.filtro="";
		return getDatosEspecie(ident);
	}

	private DatosDeEspecieResponse getDatosEspecie(String ident) {
		DatosDeEspecieResponse response = new DatosDeEspecieResponse();

		response.setIdentTaxon(ident);
		response.setDatosTaxon(repo.leeDatosTaxon(ident));
		response.setCitas(repo.getList(ident));
		response.setFotos(repo.getListFotos(ident));
		response.setSinonimos(repo.getListSinonimos(ident));
		response.setComunes(repo.getListComunes(ident));
		response.setInfo(repo.leeInfo(ident));
		response.setFiltro();
		return response;
	}

}
