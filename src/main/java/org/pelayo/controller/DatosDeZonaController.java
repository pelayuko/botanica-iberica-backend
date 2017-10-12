package org.pelayo.controller;

import java.util.List;

import org.pelayo.controller.model.DatosDeZonaResponse;
import org.pelayo.controller.model.TaxonResponse;
import org.pelayo.dao.DatosDeZonaRepository;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeZonaController {

	@Autowired
	private TaxonesRepository taxonesRepo;

	@Autowired
	private DatosDeZonaRepository zonaRepo;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/datosDeZona")
	public DatosDeZonaResponse datosDeZona(@RequestParam(value = "zona", required = true) String zona,
			@RequestParam(value = "sector", required = true) String sector) {
		DatosDeZonaResponse resp = new DatosDeZonaResponse();

		resp.setNombreZona(zona);
		resp.setFotos(zonaRepo.getListFotos(zona, sector));
		resp.setDescripcion(zonaRepo.getComentario(zona));
		return resp;
	}

	@RequestMapping("/taxonesDeZona")
	public List<TaxonResponse> taxonesDeZona(@RequestParam(value = "zona", required = true) String zona,
			@RequestParam(value = "sector", required = true) String sector) {
		return taxonesRepo.taxonesByZona(zona, sector);
	}

	@RequestMapping("/datosDeZonaFromUTM")
	public DatosDeZonaResponse datosDeZonaFromUTM(@RequestParam(value = "utm", required = true) String utm,
			@RequestParam(value = "sector", required = true) String sector) {
		String zona = zonaRepo.byUtm(utm);

		DatosDeZonaResponse resp = new DatosDeZonaResponse();

		resp.setNombreZona(zona);
		resp.setFotos(zonaRepo.getListFotos(zona, sector));
		resp.setDescripcion(zonaRepo.getComentario(zona));
		return resp;
	}

	@RequestMapping("/taxonesDeZonaFromUTM")
	public List<TaxonResponse> taxonesDeZonaFromUTM(@RequestParam(value = "utm", required = true) String utm,
			@RequestParam(value = "sector", required = true) String sector) {
		String zona = zonaRepo.byUtm(utm);
		return taxonesRepo.taxonesByZona(zona, sector);
	}

}
