package org.pelayo.controller;

import org.pelayo.controller.model.DatosDeZonaResponse;
import org.pelayo.dao.DatosDeZonaRepository;
import org.pelayo.dao.ListaCitasRepository;
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
		String nombreZona = zona;
		if (zona.startsWith("Todo el sector")) {
			int temp = (int) (sector.charAt(sector.length() - 1)) - 64;
			if (temp < 10)
				nombreZona = "Todo el sector (" + ListaCitasRepository.SECTORES[temp - 1][1] + ")";
			else
				nombreZona = "Todo el sector (Otras)";
		}

		DatosDeZonaResponse resp = new DatosDeZonaResponse();

		resp.setNombreZona(nombreZona);
		resp.setEspecies(taxonesRepo.taxonesByZona(zona, sector));
		resp.setFotos(zonaRepo.getListFotos(zona, sector));
		resp.setDescripcion(zonaRepo.getComentario(zona));
		return resp;
	}

}
