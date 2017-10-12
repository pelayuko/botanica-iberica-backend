package org.pelayo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.pelayo.controller.model.FotoResponse;
import org.pelayo.controller.model.SearchResponse;
import org.pelayo.controller.model.SearchResponse.SearchType;
import org.pelayo.controller.model.TaxonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class TaxonesRepository {

	// FIXME: simplify, hibernate, reuse, entities not views...

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DatosDeEspecieRepository datosDeEspecieRepository;

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

		final String query = "select elNombre from consespecie where elNombre like" + param + " limit " + limit;
		return jdbcTemplate.query(query, new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.ESPECIE).withTaxon(
						new TaxonResponse(rs.getString("elNombre"), rs.getString("elNombre")));
			}
		});
	}

	public List<SearchResponse> buscaNombreGenero(String busquedaGenero, int limit) {
		String query = "select NombreGen from géneros where NombreGen like '" + busquedaGenero
				+ "%' and not sinEspecies limit " + limit;
		return jdbcTemplate.query(query, new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.GENERO).withResult(rs.getString("NombreGen"));
			}
		});
	}

	public List<TaxonResponse> getTaxonesByGenero(String genero) {
		String query = "select identEsp,Género,elNombre, Familia, idPirineos from consespecie where Género = '" + genero + "'";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TaxonResponse(rs.getString("elNombre"), rs.getString("idPirineos"), rs.getString("Género"), rs
						.getString("Familia"));
			}
		});
	}

	public List<SearchResponse> buscaNombreFamilia(String busquedaFamilia, int limit) {
		String query = "select Nombre, NAceptado from denomfamilias where "
				+ "TipoDenom in ('Familia','Sinónimo') and "
				+ "Nombre like '" + busquedaFamilia + "%' limit " + limit;
		return jdbcTemplate.query(query, new RowMapper<SearchResponse>() {
			@Override
			public SearchResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				return SearchResponse.mk(SearchType.FAMILIA).withTaxon(
						new TaxonResponse(rs.getString("NAceptado"), rs.getString("Nombre")));//withResult(rs.getString("NombreFam"));
			}
		});
	}

	public List<TaxonResponse> getTaxonesByFamilia(String familia) {
		String query = "select identEsp, Género, elNombre, Familia, ifnull(subfamilia,'-') as subfam, ifnull(tribu,'-') as trib, "
				+ " fitotipo, fitosubtipo, color from consespecie where Familia = '"
				+ familia + "' order by idPirineos";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String nombre = rs.getString("elNombre");
				TaxonResponse elTaxon = new TaxonResponse(nombre, "", rs.getString("Género"), rs.getString("Familia"),
						datosDeEspecieRepository.getRandomFoto(nombre));
				elTaxon.setFitotipo(rs.getString("fitotipo"));
				elTaxon.setFitosubtipo(rs.getString("fitosubtipo"));
				elTaxon.setColorflor(rs.getString("color"));
				elTaxon.setSubfamilia(rs.getString("subfam"));
				elTaxon.setTribu(rs.getString("trib"));
				return elTaxon;
			}
		});
	}
	public List<TaxonResponse> getGenerosByFamilia(String familia) {
		String query = "select * from (select identEsp, Género, elNombre, Familia, ifnull(subfamilia,'-') as subfam, ifnull(tribu,'-') as trib, "
				+ " fitotipo, fitosubtipo, color, idPirineos from consespecie where Familia = '"
				+ familia + "' order by rand()) as temporal group by Género order by idPirineos";
		return jdbcTemplate.query(query,

		new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String nombre = rs.getString("elNombre");
				TaxonResponse elTaxon = new TaxonResponse(nombre, "", rs.getString("Género"), rs.getString("Familia"),
						datosDeEspecieRepository.getRandomFoto(nombre));
				elTaxon.setFitotipo(rs.getString("fitotipo"));
				elTaxon.setFitosubtipo(rs.getString("fitosubtipo"));
				elTaxon.setColorflor(rs.getString("color"));
				elTaxon.setSubfamilia(rs.getString("subfam"));
				elTaxon.setTribu(rs.getString("trib"));
				return elTaxon;
			}
		});
	}

	
	public List<String> getNombreZonasBySector(String etiqsector) {
		String query = "select nombre from conszonas where Sector = '" + etiqsector + "'";
		List<String> lista = jdbcTemplate.query(query,

		new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("nombre");
			}
		});

		if (etiqsector.startsWith("Z")) {
			lista.add("Todos los sectores");
			lista.add("Fuera de la comarca");
		} else {
			String sector = jdbcTemplate.queryForObject("select denom from sectores where etiq = '" + etiqsector + "'",
					new RowMapper<String>() {
						@Override
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString("denom");
						}
					});
			lista.add("Todo el sector (" + sector + ")");
		}
		return lista;
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
			consulta = "select distinct elNombre, Familia, fitotipo, fitosubtipo, color from conscitas "
					+ "join especies on conscitas.idpirineos = especies.idpirineos";
		} else if (zona.startsWith("Todo el sector") || zona.startsWith("Fuera")) {
			// elsector=lazona.substring(lazona.length()-1, lazona.length());
			consulta = "select distinct elNombre, Familia, fitotipo, fitosubtipo, color from conscitas "
					+ "join especies on conscitas.idpirineos = especies.idpirineos where Sector= '" + sector + "'";
		} else {
			consulta = "select distinct elNombre, Familia, fitotipo, fitosubtipo, color from conscitas "
					+ "join especies on conscitas.idpirineos = especies.idpirineos where laZona= '" + zona + "'";
		}
		List<TaxonResponse> results = jdbcTemplate.query(consulta, new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String nombre = rs.getString("elNombre");
				TaxonResponse elTaxon = new TaxonResponse(nombre, "", nombre, rs.getString("Familia"), datosDeEspecieRepository
						.getRandomFoto(nombre));
				elTaxon.setFitotipo(rs.getString("fitotipo"));
				elTaxon.setFitosubtipo(rs.getString("fitosubtipo"));
				elTaxon.setColorflor(rs.getString("color"));
				return elTaxon;
			}
		});
		return results;
	}

	public List<TaxonResponse> taxonesByUTM(String utm) {
		String consulta, lautm;
		if (utm.length() == 7) {
			lautm = utm.substring(3, 6) + "_" + utm.substring(6) + "_";
			consulta = "select distinct elNombre, Familia, fitotipo, fitosubtipo, color from conscitas "
					+ "join especies on conscitas.idpirineos = especies.idpirineos where Coord like '" + lautm + "'";
		} else {
			lautm = utm.substring(3);
			consulta = "select distinct elNombre, Familia, fitotipo, fitosubtipo, color from conscitas "
					+ "join especies on conscitas.idpirineos = especies.idpirineos where Coord = '" + lautm + "'";
		}
		List<TaxonResponse> results = jdbcTemplate.query(consulta, new RowMapper<TaxonResponse>() {
			@Override
			public TaxonResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				String nombre = rs.getString("elNombre");
				String genero = nombre.substring(0, nombre.indexOf(" "));

				FotoResponse randomFoto = datosDeEspecieRepository.getRandomFoto(nombre);

				TaxonResponse elTaxon = new TaxonResponse(nombre, "", genero, rs.getString("Familia"), randomFoto);
				elTaxon.setFitotipo(rs.getString("fitotipo"));
				elTaxon.setFitosubtipo(rs.getString("fitosubtipo"));
				elTaxon.setColorflor(rs.getString("color"));
				return elTaxon;
			}
		});
		return results;
	}
	
}
