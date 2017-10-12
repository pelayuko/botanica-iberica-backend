package org.pelayo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.FotoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class DatosDeZonaRepository {

	private static final Logger log = Logger.getLogger(DatosDeZonaRepository.class);

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<FotoResponse> getListFotos(String zona, String sector) {
		String consulta;
		if (zona.startsWith("Todos los sectores")) {
			consulta = "select flickrUrl, ifnull(comentario,'sin coment.') as coment, UTM from consfotoslugares order by rand() limit 15";

		} else if (zona.startsWith("Todo el sector") || zona.startsWith("Fuera")) {
			consulta = "select flickrUrl, ifnull(comentario,'sin coment.') as coment, UTM from consfotoslugares join sectores on etiq=Sector where Sector= '"
					+ sector + "' order by rand() limit 15";
		} else {

			String idzona = jdbcTemplate.queryForObject("select Id from zonas where nombre = '" + zona + "'",
					new RowMapper<String>() {
						@Override
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString("Id");
						}
					});

			consulta = "select flickrUrl, ifnull(comentario,'sin coment.') as coment, UTM from consfotoslugares "
					+ "join sectores on etiq=Sector where Zona = '" + idzona + "' order by rand() limit 15"; // hace falta el sector, pero no se restringe
		}
		List<FotoResponse> results = jdbcTemplate.query(consulta, new RowMapper<FotoResponse>() {
			@Override
			public FotoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new FotoResponse(rs.getString("flickrUrl"), rs.getString("coment"), rs.getString("UTM"));
			}
		});
		return results;

	}

	public String getComentario(String zona) {
		String comentario;
		if (zona.startsWith("Todos los sectores")) {
			comentario = "Todos los sectores en la comarca";
		} else if (zona.startsWith("Fuera")) {
			comentario = "Fuera de la comarca";
		} else if (zona.startsWith("Todo el sector")) {
			String elsector = zona.substring(zona.indexOf('(') + 1, zona.length() - 1);
			//String elsector = zona.substring(zona.length() - 1, zona.length());
			comentario = jdbcTemplate.queryForObject("select Descrip from sectores where Denom = '" + elsector + "'",
					new RowMapper<String>() {
						@Override
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString("Descrip");
						}
					});
		} else {
			comentario = jdbcTemplate.queryForObject("select descripción from zonas where nombre = '" + zona + "'",
					new RowMapper<String>() {
						@Override
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString("descripción");
						}
					});
		}

		return comentario;
	}

	public List<FotoResponse> fotosDeZonasAlAzar(int limit) {
		String consulta = "select flickrUrl, Sector, ifnull(comentario,'sin coment.') as coment, ifnull(UTM,'-') as UTM, zonas.nombre "
				+ "from consfotoslugares join zonas on consfotoslugares.zona = zonas.id order by rand() limit " + limit;
		List<FotoResponse> results = jdbcTemplate.query(consulta, 
				new RowMapper<FotoResponse>() {
					@Override
					public FotoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
						String temp = rs.getString("flickrUrl");
						return new FotoResponse(temp, rs.getString("coment"), rs.getString("UTM"), "Sector "
								+ rs.getString("Sector"), rs.getString("zonas.nombre"));
					}
				});
		return results;
	}
	
	public List<FotoResponse> fotosDeZonasPorTema(String tema, int limit) {
		String consulta = "select flickrUrl, Sector, ifnull(comentario,'sin coment.') as coment, ifnull(UTM,'-') as UTM, zonas.nombre "
				+ "from consfotoslugares join zonas on consfotoslugares.zona = zonas.id where "
				+ "comentario like '" + tema + "%' or comentario like '% " + tema + "%' or comentario like '%(" + tema + "%' or comentario like '%''" + tema + "%' "
				+ "order by rand() limit " + limit;
		List<FotoResponse> results = jdbcTemplate.query(consulta, 
				new RowMapper<FotoResponse>() {
					@Override
					public FotoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
						String temp = rs.getString("flickrUrl");
						return new FotoResponse(temp, rs.getString("coment"), rs.getString("UTM"), "Sector "
								+ rs.getString("Sector"), rs.getString("zonas.nombre"));
					}
				});
		return results;
	}
	
	public List<String> listaTemas(final String clave, int limit) {
		Set<String> hs = new HashSet<>();
		String consulta = "select ifnull(comentario,'sin coment.') as coment from fotoslugares "
				+ "where comentario like '" + clave + "%' limit " + limit;
		List<String> results = jdbcTemplate.query(consulta, 
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return limpiaString(rs.getString("coment"), ".");
					}
				});
		hs.addAll(results);
		
		results.clear();
		consulta = "select substring(comentario, locate('("+ clave + "',comentario)) as coment from fotoslugares "
				+ "where comentario like '%(" + clave +"%' limit " + limit;
		results = jdbcTemplate.query(consulta, 
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return limpiaString(rs.getString("coment"), "(");
					}
				});
		hs.addAll(results);
		
		results.clear();
		consulta = "select substring(comentario, locate('''"+ clave + "',comentario)) as coment from fotoslugares "
				+ "where comentario like '%''" + clave +"%' limit " + limit;
		results = jdbcTemplate.query(consulta, 
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return limpiaString(rs.getString("coment"), "'");
					}
				});
		hs.addAll(results);
		
		results.clear();
		consulta = "select substring(comentario, locate(' "+ clave + "',comentario)) as coment from fotoslugares "
				+ "where comentario like '% " + clave +"%' limit " + limit;
		results = jdbcTemplate.query(consulta, 
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return limpiaString(rs.getString("coment"), " ");
					}
				});
		hs.addAll(results);
		
		results.clear();
		
		results.addAll(hs);
		return results;
	}
	
	private String limpiaString(String sucia, String inicial) {
		String limpia = sucia.substring(sucia.indexOf(inicial) + 1, sucia.length());
		int limit = limpia.indexOf(".");
		if (limit != -1) limpia = limpia.substring(0, limit);
		limit = limpia.indexOf(":");
		if (limit != -1) limpia = limpia.substring(0, limit);
		limit = limpia.indexOf(",");
		if (limit != -1) limpia = limpia.substring(0, limit);
		limit = limpia.indexOf(")");
		if (limit != -1) limpia = limpia.substring(0, limit);
		limit = limpia.indexOf("'");
		if (limit != -1) limpia = limpia.substring(0, limit);
		limit = limpia.indexOf(" ");
		if (limit != -1) limpia = limpia.substring(0, limit);
		return limpia;
	}
	
	public String byUtm(String utm) {
		String query = "select nombre from fotoslugares join zonas on zonas.id = fotoslugares.zona where Coord = '"
				+ utm + "' limit 1";
		String zona = jdbcTemplate.queryForObject(query, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("nombre");
			}
		});
		return zona;
	}
}
