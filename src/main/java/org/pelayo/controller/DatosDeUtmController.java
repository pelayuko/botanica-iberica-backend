package org.pelayo.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.pelayo.controller.model.DatosDeUTMResponse;
import org.pelayo.controller.model.TaxonResponse;
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
//		resp.setEspecies(repo.taxonesByUTM(utm));
		String consulta, lautm;	
		String sector = "-"; // para UTMs10x10
		if (utm.length() == 9) {
			lautm = utm.substring(3);
/*			consulta = "select sectores.denom as elSector from utmsectores join sectores on sectores.etiq = utmsectores.sector where utm = '"
						+ lautm +"' limit 1";
			sector = jdbcTemplate.queryForObject(consulta,
					new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("elSector");
				}
			});
*/ 		// Por fin, no paso el sector, porque no tiene interés
		}
		else lautm = utm.substring(3, 6) + "_" + utm.substring(6) + "_";
		consulta = "select distinct laZona from conscitas where coord like '" + lautm +"' and idzona <> 0";
		List<String> zonas = jdbcTemplate.query(consulta,
				new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("laZona");
			}
		});
		resp.setLasZonas(zonas);
		resp.setElSector(sector);
        return resp;
    }
    
	@RequestMapping("/taxonesDeUtm")
	public List<TaxonResponse> taxonesDeUtm(@RequestParam(value = "utm", required = true) String utm) {
		return repo.taxonesByUTM(utm);
	}
}
