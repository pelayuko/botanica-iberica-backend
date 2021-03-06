package org.pelayo.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.CitaResponse;
import org.pelayo.controller.model.FotoResponse;
import org.pelayo.controller.model.InfoTaxonResponse;
import org.pelayo.controller.model.TaxonResponse;
import org.pelayo.model.Especie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class DatosDeEspecieRepository {

	private static final Logger log = Logger.getLogger(DatosDeEspecieRepository.class);

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;

	public String relativeTaxon(String actual, boolean prev, String filtro) {
		String idPirineos = "";
		try {
			actual = URLDecoder.decode(actual, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		idPirineos = jdbcTemplate.queryForObject(
				"select idPirineos from consespecie where elNombre = '" + actual + "'", new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString("idPirineos");
					}
				});
		String comparator = prev ? " < " : " > ";
		String result = "";
		String consulta = "select elNombre from consespecie where idPirineos " + comparator + "'" + idPirineos
				+ "' and not foranea" + filtro;
		consulta += " order by idPirineos " + (prev ? "desc" : "asc") + " limit 1";
		try {
			result = jdbcTemplate.queryForObject(consulta, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("elNombre");
				}
			});
		} catch (EmptyResultDataAccessException e) {
			result = "";
		}
		if (result.isEmpty()) {
			if (prev)
				idPirineos = "9999";
			else
				idPirineos = "0000";
			consulta = "select elNombre from consespecie where idPirineos " + comparator + "'" + idPirineos
					+ "' and not foranea" + filtro;
			consulta += " order by idPirineos " + (prev ? "desc" : "asc") + " limit 1";

			result = jdbcTemplate.queryForObject(consulta, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("elNombre");
				}
			});
		}
		return result;
	}

	public String taxonAlAzar(String filtro) {
		String result = jdbcTemplate.queryForObject("select elNombre from consespecie where not foranea" + filtro
				+ " order by RAND() limit 1", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("elNombre");
			}
		});
		return result;
	}

	public InfoTaxonResponse leeInfo(final String ident) {

		String query = "select *, FT.tipo as fitoTipoNombre, FST.Subtipo as fitoSubtipoNombre from consespecie C, fitotipos FT, fitosubtipos FST "
				+ " where C.elNombre = '" + ident + "' and C.fitoTipo = FT.Abrev and C.fitoSubtipo = FST.Abrev ";
		List<InfoTaxonResponse> resultado = jdbcTemplate.query(query, new RowMapper<InfoTaxonResponse>() {
			@Override
			public InfoTaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new InfoTaxonResponse(rs.getString("color"), rs.getString("fitoTipoNombre"), rs
						.getString("fitoSubtipoNombre"), rs.getString("presComarca"), rs.getString("frecComarca"), rs
						.getString("comentario"));
			}
		});

		if (resultado.isEmpty()) {
			log.error("Unable to fetch info for id " + ident);
			return null;
		}

		return resultado.get(0);
	}

	public TaxonResponse leeDatosTaxon(final String ident) {
		String prueba = "select losAutores, Género, Familia, idPirineos from consespecie where elNombre = '" + ident + "'";
		TaxonResponse result = jdbcTemplate.queryForObject(prueba,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(ident, rs.getString("idPirineos"), rs.getString("Género"), rs.getString("Familia"));
			}
		});
		return result;
	}

	public List<CitaResponse> getList(String elTaxon) {
		String consulta = "select ifnull(Coord,'-') as coord, Sector, laZona, Fecha, Lugar from conscitas where elNombre= '"
				+ elTaxon + "'";
		List<CitaResponse> results = jdbcTemplate.query(consulta,

		new RowMapper<CitaResponse>() {
			@Override
			public CitaResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String lautm = rs.getString("coord");
				if (lautm.equals("-")) {
					lautm = "*";
				} else {
					lautm = "30T" + lautm;
				}
				String elsector = rs.getString("Sector");
				// elsector = Character.toString((char) (48 + (int)
				// (elsector.charAt(0)) - 64));
				CitaResponse lacita = new CitaResponse(lautm, elsector);
				String lazona = rs.getString("laZona");
				lacita.setlaZona(lazona);
				lacita.laFecha = rs.getDate("Fecha").toString();
				lacita.elLugar = rs.getString("Lugar");
				return lacita;
			}
		});
		return results;

	}

	public List<FotoResponse> getListFotos(String elTaxon) {
		return getListFotos(elTaxon, 50);
	}

	public FotoResponse getRandomFoto(String elTaxon) {
		List<FotoResponse> listFotos = getListFotos(elTaxon, 1);
		if (listFotos.isEmpty()) {
			return null;
		}

		return listFotos.get(0);
	}

	public List<FotoResponse> getListFotos(String elTaxon, int limit) {
		String consulta = "select flickrUrl, denom, ifnull(comentario,'sin coment.') as coment, ifnull(UTM,'-') as UTM from consfotos join sectores on etiq=sector where elnombre = '"
				+ elTaxon + "' order by RAND() limit " + limit;
		List<FotoResponse> results = jdbcTemplate.query(consulta, new RowMapper<FotoResponse>() {
			@Override
			public FotoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new FotoResponse(rs.getString("flickrUrl"), rs.getString("coment"), rs.getString("UTM"));
			}
		});
		return results;

	}

	public List<FotoResponse> fotosDeEspecieAlAzar(int limit) {
		String consulta = "select flickrUrl, elNombre, ifnull(comentario,'sin coment.') as coment, ifnull(UTM,'-') as UTM from consfotos order by rand() limit "
				+ limit;
		List<FotoResponse> results = jdbcTemplate.query(consulta, new RowMapper<FotoResponse>() {
			@Override
			public FotoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String temp = rs.getString("flickrUrl");
				return new FotoResponse(temp, rs.getString("coment"), rs.getString("UTM"), rs.getString("elNombre"));
			}
		});
		return results;
	}

	public List<String> getListSinonimos(String elTaxon) {
		String consulta = "select elNombre, ifnull(Autores,'') as Autores from consinonimos" + " where nomaceptado = '"
				+ elTaxon + "'"; // Aquí sí irán los autores
		List<String> results = jdbcTemplate.query(consulta,

		new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("elNombre") + " " + rs.getString("Autores");
			}
		});
		return results;

	}

	public List<String> getListComunes(String elTaxon) {
		String consulta = "select Nombre as ncomun from conscomunes where elNombre = '" + elTaxon + "'";
		List<String> results = jdbcTemplate.query(consulta,

		new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("ncomun");
			}
		});
		return results;

	}
}
