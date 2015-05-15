package org.pelayo.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.pelayo.controller.model.DatosDeUTMResponse;
import org.pelayo.dao.DatosDeZonaRepository;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeUtmController {

	@Autowired
	private DatosDeZonaRepository zonaRepo;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TaxonesRepository repo;

    @RequestMapping("/datosDeUtm")
    public DatosDeUTMResponse datosDeUtm(@RequestParam(value = "utm", required = true) String utm) {
		DatosDeUTMResponse resp = new DatosDeUTMResponse();
		resp.setUTM(utm);
		resp.setEspecies(repo.taxonesByUTM(utm));
		String sector = "-"; // para UTMs10x10
		if (utm.length() == 9) {
			String query = "select sectores.denom as elSector from utmsectores join sectores on sectores.etiq = utmsectores.sector where utm = '"
						+ utm.substring(3) +"' limit 1";
			sector = jdbcTemplate.queryForObject(query,
					new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("elSector");
				}
			});
		}
		resp.setElSector(sector);
        return resp;
    }
        
}
