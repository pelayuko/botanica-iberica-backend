package org.pelayo.controller;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.DatosDeEspecieResponse;
import org.pelayo.dao.DatosDeEspecieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class DatosDeEspecieController {
	
	private static final Logger log = Logger.getLogger(DatosDeEspecieController.class);

	@Autowired
	private DatosDeEspecieRepository repo;

	@RequestMapping("/datosDeEspecieRandom")
	public DatosDeEspecieResponse datosDeEspecieRandom(@RequestParam(value = "filtro", required = true) String filtro) {
		if (filtro.isEmpty()) filtro="";
		return getDatosEspecie(repo.taxonAlAzar(filtro));
	}

	@RequestMapping("/datosDeEspecieNext")
	public DatosDeEspecieResponse datosDeEspecieNext(@RequestParam(value = "ident", required = true) String ident,
			@RequestParam(value = "filtro", required = true) String filtro) {
		return getDatosEspecie(repo.relativeTaxon(ident, false, filtro));
	}

	@RequestMapping("/datosDeEspeciePrev")
	public DatosDeEspecieResponse datosDeEspeciePrev(@RequestParam(value = "ident", required = true) String ident,
			@RequestParam(value = "filtro", required = true) String filtro) {
		return getDatosEspecie(repo.relativeTaxon(ident, true, filtro));
	}

	@RequestMapping("/datosDeEspecie")
	public DatosDeEspecieResponse datosDeEspecie(@RequestParam(value = "ident", required = true) String ident) {
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
		return response;
	}

}
