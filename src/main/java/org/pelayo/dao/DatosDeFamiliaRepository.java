package org.pelayo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.controller.model.CitaResponse;
import org.pelayo.controller.model.FotoResponse;
import org.pelayo.controller.model.InfoTaxonResponse;
import org.pelayo.controller.model.TaxonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class DatosDeFamiliaRepository {

	private static final Logger log = Logger.getLogger(DatosDeFamiliaRepository.class);

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DatosDeEspecieRepository datosDeEspecieRepository;

	public List<FotoResponse> getListFotos(String familia, int limit) {
		String consulta = "select flickrUrl, elNombre, ifnull(comentario,'sin coment.') as coment, ifnull(UTM,'-') as UTM from consfotos where Familia = '"
				+ familia + "' order by rand() limit " + limit;
		List<FotoResponse> results = jdbcTemplate.query(consulta, new RowMapper<FotoResponse>() {
			@Override
			public FotoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String temp = rs.getString("flickrUrl");
				return new FotoResponse(temp, rs.getString("coment"), rs.getString("UTM"), rs.getString("elNombre"));
			}
		});
		return results;

	}

	public String getRefFlora(String laFamilia) {
		String query = "select refFloraIberica as refFlIb from familias where NombreFam = '" + laFamilia + "'";
		log.debug("Get Ref flora - query: " + query);

		List<String> flora = jdbcTemplate.query(query, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("refFlIb");
			}
		});

		if (flora.isEmpty()) {
			log.warn("Ref flora is empty for familia: " + laFamilia);

			return "http://www.floraiberica.es";
		} else {
			return "http://www.floraiberica.es/floraiberica/texto/pdfs/" + flora.get(0) + ".pdf";
		}
	}

	public String getGrupoFam(String laFamilia) {
		String grupo = jdbcTemplate.queryForObject("select grupoFam from familias where NombreFam = '" + laFamilia
				+ "'", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("grupoFam");
			}
		});
		return grupo;
	}
	
	public List<String> getListSinonimos(String laFamilia) {
		String consulta = "select Nombre from denomfamilias where TipoDenom = 'Sinónimo' and NAceptado = '"
				+ laFamilia + "'"; // Aquí sí irán los autores
		List<String> results = jdbcTemplate.query(consulta,

		new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("Nombre");
			}
		});
		return results;

	}
	
	public List<String> getListSubfamilias(String laFamilia) {
		String consulta = "select Subfamilia from subfamilias where Familia = '"
				+ laFamilia + "'"; // Aquí sí irán los autores
		List<String> results = jdbcTemplate.query(consulta,

		new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("Subfamilia");
			}
		});
		return results;

	}

}
