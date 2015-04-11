package org.pelayo.controller.model;

public class SearchResponse {
	public enum SearchType {
		ESPECIE, GENERO, ZONA, ESPECIE_COMUN, FAMILIA
	}

	private SearchType type;

	private TaxonResponse taxon;

	private String result;

	public static SearchResponse mk(SearchType type) {
		SearchResponse sr = new SearchResponse();
		sr.setType(type);
		return sr;
	}

	public SearchType getType() {
		return type;
	}

	public void setType(SearchType type) {
		this.type = type;
	}

	public SearchResponse withType(SearchType type) {
		this.type = type;
		return this;
	}

	public TaxonResponse getTaxon() {
		return taxon;
	}

	public void setTaxon(TaxonResponse taxon) {
		this.taxon = taxon;
	}

	public SearchResponse withTaxon(TaxonResponse taxon) {
		this.taxon = taxon;
		return this;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public SearchResponse withResult(String result) {
		this.result = result;
		return this;
	}

	public String getLabel() {
		StringBuilder sb = new StringBuilder();

		switch (type) {
		case ESPECIE:
			sb.append("Especie - ");
			break;
		case ESPECIE_COMUN:
			sb.append("Nom comun - ");
			break;
		case GENERO:
			sb.append("Gen - ");
			break;
		case FAMILIA:
			sb.append("Fam - ");
			break;
		case ZONA:
			sb.append("Zona - ");
			break;
		default:
			break;
		}

		sb.append(getSimpleLabel());

		return sb.toString();
	}

	public String getSimpleLabel() {
		StringBuilder sb = new StringBuilder();

		switch (type) {
		case ESPECIE:
		case ESPECIE_COMUN:
			sb.append(taxon.getNombre());
			break;
		case GENERO:
		case FAMILIA:
		case ZONA:
			sb.append(result);
			break;
		default:
			break;
		}

		return sb.toString();
	}
}
