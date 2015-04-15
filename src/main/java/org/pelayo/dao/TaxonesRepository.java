package org.pelayo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.pelayo.controller.model.SearchResponse;
import org.pelayo.controller.model.SearchResponse.SearchType;
import org.pelayo.controller.model.TaxonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@Transactional(readOnly = true)
public class TaxonesRepository {

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<TaxonResponse> taxonesByNombre(String nombre) {
		String query = "select elNombre, nomaceptado from consinonimos where elNombre = '" + nombre + "'";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("nomaceptado"), rs.getString("elNombre"));
			}
		});
	}

	public List<TaxonResponse> taxonesByNombreComun(String nombreComun) {
		String query = "select elNombre, Nombre from conscomunes where Nombre = '" + nombreComun + "'";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("elNombre"), rs.getString("Nombre"));
			}
		});
	}

	public List<SearchResponse> buscaSinonimos(String nombreConSinonimos, int limit) {
		String query = "select elNombre, nomaceptado from consinonimos where elNombre like '" + nombreConSinonimos
				+ "%' limit " + limit;
		return jdbcTemplate.query(query,

		new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.ESPECIE).withTaxon(
						new TaxonResponse(rs.getString("nomaceptado"), rs.getString("elNombre")));
			}
		});
	}

	public List<SearchResponse> buscaNombreComun(String nombreComun, int limit) {
		String query = "select Nombre, elNombre from conscomunes where Nombre like '%" + nombreComun + "%' limit "
				+ limit;
		return jdbcTemplate.query(query,

		new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.ESPECIE_COMUN).withTaxon(
						new TaxonResponse(rs.getString("elNombre"), rs.getString("Nombre")));
			}
		});
	}

	public List<SearchResponse> buscaNombreEspecie(String nombreEspecie, int limit, boolean starting) {
		String param = (starting ? "'" : "'%") + nombreEspecie + "%'";
		
		final String query = "select elNombre from ConsEspecie where elNombre like" + param + " limit "
				+ limit;
		return jdbcTemplate.query(query, new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.ESPECIE).withTaxon(
						new TaxonResponse(rs.getString("elNombre"), rs.getString("elNombre")));
			}
		});
	}

	public List<SearchResponse> buscaNombreGenero(String busquedaGenero, int limit) {
		String query = "select NombreGen from Géneros where NombreGen like '" + busquedaGenero
				+ "%' and not sinEspecies limit " + limit;
		return jdbcTemplate.query(query, new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.GENERO).withResult(rs.getString("NombreGen"));
			}
		});
	}

	public List<TaxonResponse> getTaxonesByGenero(String genero) {
		String query = "select identEsp,Género,elNombre, Familia from ConsEspecie where Género = '" + genero + "'";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("elNombre"), rs.getString("elNombre"), rs.getString("Género"), rs.getString("Familia"));
			}
		});
	}

	public List<SearchResponse> buscaNombreFamilia(String busquedaFamilia, int limit) {
		String query = "select NombreFam from Familias where NombreFam like '" + busquedaFamilia
				+ "%' limit " + limit;
		return jdbcTemplate.query(query, new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.FAMILIA).withResult(rs.getString("NombreFam"));
			}
		});
	}

	public List<TaxonResponse> getTaxonesByFamilia(String familia) {
		String query = "select identEsp,Género,elNombre, Familia from ConsEspecie where Familia = '" + familia + "'";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("elNombre"), rs.getString("Género"), rs.getString("Familia"));
			}
		});
	}
	
	public List<String> getNombreZonasBySector(String sector) {
		String query = "select nombre from conszonas where Sector = '" + sector + "'";
		return jdbcTemplate.query(query,

		new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("nombre");
			}
		});
	}

	public List<SearchResponse> buscaNombreZonas(String zona) {
		String query = "select nombre from conszonas where nombre like '%" + zona + "%'";
		return jdbcTemplate.query(query,

		new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.ZONA).withResult(rs.getString("nombre"));
			}
		});
	}

	public List<TaxonResponse> taxonesByZona(String zona, String sector) {
		String consulta;
		if (zona.startsWith("Todos los sectores")) {
			consulta = "select distinct elNombre, Familia from conscitas";
		} else if (zona.startsWith("Todo el sector") || zona.startsWith("Fuera")) {
			// elsector=lazona.substring(lazona.length()-1, lazona.length());
			consulta = "select distinct elNombre, Familia from conscitas where Sector= '" + sector + "'";
		} else {
			consulta = "select distinct elNombre, Familia from conscitas where laZona= '" + zona + "'";
		}
		List<TaxonResponse> results = jdbcTemplate.query(consulta, new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("elNombre"), "", rs.getString("elNombre"), rs.getString("Familia"));
			}
		});
		return results;
	}

	public List<TaxonResponse> taxonesByUTM(String utm) {
		String consulta, lautm;
		if (utm.length() == 7) {
			lautm = utm.substring(3, 6) + "_" + utm.substring(6) + "_";
			consulta = "select distinct elNombre, Familia from conscitas where Coord like '" + lautm + "'";
		} else {
			lautm = utm.substring(3);
			consulta = "select distinct elNombre, Familia from conscitas where Coord = '" + lautm + "'";
		}
		List<TaxonResponse> results = jdbcTemplate.query(consulta, new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("elNombre"), "", rs.getString("elNombre"), rs.getString("Familia"));
			}
		});
		return results;
	}
}