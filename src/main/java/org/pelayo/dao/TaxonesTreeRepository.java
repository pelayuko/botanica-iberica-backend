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
		String query = "select NombreFam, consubfam from Familias where GrupoFam = '" + parent + "'";
		List<TaxonLeaf> result = jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				boolean flag = rs.getBoolean("consubfam");
				return new TaxonLeaf(rs.getString("NombreFam"), flag?"subfamily":"genus");
			}
		});
		return result;
	}
	
	public List<TaxonLeaf> taxonLeafsBySubfamily(String parent) {
		String query = "select SubFamilia from SubFamilias where Familia = '" + parent + "'";
		List<TaxonLeaf> result = jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonLeaf(rs.getString("SubFamilia"), "tribu");
			}
		});
		if (result.isEmpty()) result = taxonLeafsByGenus(parent); // no hay subfamilias (ni tribus, por tanto)
		return result;
	}
	
	public List<TaxonLeaf> taxonLeafsByTribu(String parent) {
		String query = "select Tribu from Tribus where SubFamilia = '" + parent + "'";
		List<TaxonLeaf> result = jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonLeaf(rs.getString("Tribu"), "genus");
			}
		});
		if (result.isEmpty()) result = taxonLeafsByGenus(parent); // no hay tribus (pero si subfamilia)
		return result;
	}
	
	public List<TaxonLeaf> taxonLeafsByGenus(String parent) {
		String query = "select NombreGen from Géneros where Tribu = '" + parent + "' and not sinEspecies";
		List<TaxonLeaf> result = jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
			@Override
			public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonLeaf(rs.getString("NombreGen"), "species");
			}
		});
		if (result.isEmpty()){
			query = "select NombreGen from Géneros where Subfamilia = '" + parent + "' and not sinEspecies";
			result = jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
				@Override
				public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new TaxonLeaf(rs.getString("NombreGen"), "species");
				}
			});
			if (result.isEmpty()){
				query = "select NombreGen from Géneros where Familia = '" + parent + "' and not sinEspecies";
				result = jdbcTemplate.query(query, new RowMapper<TaxonLeaf>() {
					@Override
					public TaxonLeaf mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new TaxonLeaf(rs.getString("NombreGen"), "species");
					}
				});
			}
		}
		return result;
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
