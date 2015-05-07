package org.pelayo.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.pelayo.controller.model.DatosDeZonaResponse;
import org.pelayo.dao.DatosDeZonaRepository;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
//		String nombreZona = zona;

		DatosDeZonaResponse resp = new DatosDeZonaResponse();

		resp.setNombreZona(zona);
		resp.setEspecies(taxonesRepo.taxonesByZona(zona, sector));
		resp.setFotos(zonaRepo.getListFotos(zona, sector));
		resp.setDescripcion(zonaRepo.getComentario(zona));
		return resp;
	}

	@RequestMapping("/datosDeZonaFromUTM")
	public DatosDeZonaResponse datosDeZonaFromUTM(@RequestParam(value = "utm", required = true) String utm,
			@RequestParam(value = "sector", required = true) String sector) {
		String query = "select nombre from fotoslugares join zonas on zonas.id = fotoslugares.zona where Coord = '" + utm +"' limit 1";
		String zona = jdbcTemplate.queryForObject(query,
				new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("nombre");
			}
		});
		
		DatosDeZonaResponse resp = new DatosDeZonaResponse();

		resp.setNombreZona(zona);
		resp.setEspecies(taxonesRepo.taxonesByZona(zona, sector));
		resp.setFotos(zonaRepo.getListFotos(zona, sector));
		resp.setDescripcion(zonaRepo.getComentario(zona));
		return resp;
	}
}
