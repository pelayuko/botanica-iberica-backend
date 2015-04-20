package org.pelayo.controller;

import org.pelayo.controller.model.DatosDeZonaResponse;
import org.pelayo.dao.DatosDeZonaRepository;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeZonaController {

	@Autowired
	private TaxonesRepository taxonesRepo;

	@Autowired
	private DatosDeZonaRepository zonaRepo;

	@RequestMapping("/datosDeZona")
	public DatosDeZonaResponse datosDeZona(@RequestParam(value = "zona", required = true) String zona,
			@RequestParam(value = "sector", required = true) String sector) {
//		String nombreZona = zona;

		DatosDeZonaResponse resp = new DatosDeZonaResponse();

		resp.setNombreZona(zona);
		resp.setEspecies(taxonesRepo.taxonesByZona(zona, sector));
		resp.setFotos(zonaRepo.getListFotos(zona, sector));
		resp.setDescripcion(zonaRepo.getComentario(zona));
		return resp;
	}

}
