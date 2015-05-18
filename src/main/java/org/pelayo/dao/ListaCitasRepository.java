package org.pelayo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.CitaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ListaCitasRepository {

	private static final Logger log = Logger.getLogger(ListaCitasRepository.class);

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<CitaResponse> listaCitasByUtmsTaxon(String taxon) {
		String consulta = "select ifnull(Coord,'-') as coord, Sector from conscitas where elNombre= '" + taxon + "'";
		return jdbcTemplate.query(consulta, new RowMapper<CitaResponse>() {
			@Override
			public CitaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String lautm = rs.getString("coord");
				if (lautm.equals("-")) {
					lautm = "*";
				} else {
					lautm = "30T" + lautm;
				}
				return new CitaResponse(lautm, convertSector(rs.getString("Sector")));
			}
		});
	}
	
	public List<CitaResponse> listaCitasJacaByUtmsTaxon(String taxon) {
		String consulta = "select idJaca from consespecie where elNombre = '" + taxon + "'";
		String codigo =
		jdbcTemplate.queryForObject(consulta, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("idJaca");							
				}
		});
		consulta = "select utm1 from jaca_comarca where cod_planta like '" + codigo + "%'";
		return jdbcTemplate.query(consulta, new RowMapper<CitaResponse>() {
			@Override
			public CitaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String lautm = rs.getString("utm1");
				return new CitaResponse(lautm, "Jaca");
			}
		});
	}
	
	public List<CitaResponse> listaCitasByUtmsZona(String zona) {
		String etiq = "", consulta;

		if (zona.startsWith("Todos los sectores")) {
			consulta = "select ifnull(Coord,'-') as coord, Sector, laZona from conscitas"; // todas
		} else if (zona.startsWith("Fuera")) {
			consulta = "select ifnull(Coord,'-') as coord, Sector, laZona from conscitas where Sector= 'Z'";
		} else if (zona.startsWith("Todo el sector")) {
			String sector = zona.substring(zona.indexOf('(') + 1, zona.length() - 1);
			etiq = jdbcTemplate.queryForObject("select etiq from sectores where denom = '" + sector + "'",
				new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("etiq");							
					}
			});
			consulta = "select ifnull(Coord,'-') as coord, Sector, laZona from conscitas where Sector= '" + etiq + "'";
		} else {
			consulta = "select ifnull(Coord,'-') as coord, Sector, laZona from conscitas where laZona= '" + zona + "'";
		}
		return jdbcTemplate.query(consulta, 
				new RowMapper<CitaResponse>() {
					@Override
					public CitaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
						String lautm = rs.getString("coord");
						if (lautm.equals("-")) {
							lautm = "*";
						} else {
							lautm = "30T" + lautm;
						}
						CitaResponse lacita = new CitaResponse(lautm, convertSector(rs.getString("Sector")));
						String lazona = rs.getString("laZona");
						lacita.setlaZona(lazona);
						return lacita;
					}


				});
	}

	public List<CitaResponse> listaCitasByUtmsSector(String sector) {
		String consulta = "select UTM, Sector from utmsectores where (Sector= '" + sector + "' and concita = 1)";
		return jdbcTemplate.query(consulta,
				new RowMapper<CitaResponse>() {
					@Override
					public CitaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
						String lautm = "30T" + rs.getString("UTM");
						CitaResponse lacita = new CitaResponse(lautm, rs.getString("Sector"));
						return lacita;
					}
				});
	}
	
	private String convertSector(String elsector) {
		return elsector; 
		// FIXME: creo que no hace falta para nada esta conversion...
		//return Character.toString((char) (48 + (int) (elsector.charAt(0)) - 64));
	}

}
