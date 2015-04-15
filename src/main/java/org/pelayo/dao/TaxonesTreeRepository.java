package org.pelayo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.pelayo.controller.model.TaxonLeaf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class TaxonesTreeRepository {

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<TaxonLeaf> taxonLeafsByGroup() {
		String query = "select NombreGrupo from GruposFam";
		return jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonLeaf(rs.getString("NombreGrupo"), "family");
			}
		});
	}

	public List<TaxonLeaf> taxonLeafsByFamily(String parent) {
		String query = "select NombreFam from Familias where GrupoFam = '" + parent + "'";
		return jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonLeaf(rs.getString("NombreFam"), "genus");
			}
		});
	}

	public List<TaxonLeaf> taxonLeafsByGenus(String parent) {
		String query = "select NombreGen,Familia from Géneros where Familia = '" + parent + "' and not sinEspecies";
		return jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonLeaf(rs.getString("NombreGen"), "species");
			}
		});
	}

	public List<TaxonLeaf> taxonLeafsBySpecy(String parent) {
		String query = "select identEsp,Género,elNombre from ConsEspecie where Género = '" + parent + "'";
		return jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonLeaf(rs.getString("elNombre"), "subspecies");
			}
		});
	}
}