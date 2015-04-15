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
public class DatosDeGeneroRepository {
	
	private static final Logger log = Logger.getLogger(DatosDeGeneroRepository.class);

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<TaxonResponse> getTaxonesByGenero(final String genero) {
		String query = "select identEsp,Género,elNombre, Familia from ConsEspecie where Género = '" + genero + "'";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("elNombre"), "", genero, rs.getString("Familia"));
			}
		});
	}

	public List<FotoResponse> getListFotos(String genero, int limit) {
		String consulta = "select flickrUrl, elNombre, ifnull(comentario,'sin coment.') as coment, ifnull(UTM,'-') as UTM from consfotos where elNombre like '"
				+ genero + "%' order by rand() limit " + limit;
		List<FotoResponse> results = jdbcTemplate.query(consulta, 
				new RowMapper<FotoResponse>() {
					@Override
					public FotoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
						String temp = rs.getString("flickrUrl");
						return new FotoResponse(temp, rs.getString("coment"), rs.getString("UTM"), rs.getString("elNombre"));
					}
				});
		return results;

	}

    public String getRefFlora(String elGenero) {		
		String flora = jdbcTemplate.queryForObject(
				"select ifnull(refFloraIberica,'-') as refFlIb from Géneros where NombreGen = '" + elGenero + "'", new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString("refFlIb");
					}
				});
		if (flora.equals("-")) return "http://www.floraiberica.es";
		else return "http://www.floraiberica.es/floraiberica/texto/pdfs/" + flora +".pdf";
    }
}
