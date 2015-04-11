package org.pelayo.controller.model;

import java.util.List;

public class DatosDeGeneroResponse {

	private List<TaxonResponse> especies;

	private String genero;

	private String familia;
	
	private String refFloraIberica;

	private List<FotoResponse> fotos;

	public List<TaxonResponse> getEspecies() {
		return especies;
	}

	public void setEspecies(List<TaxonResponse> especies) {
		this.especies = especies;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public List<FotoResponse> getFotos() {
		return fotos;
	}

	public void setFotos(List<FotoResponse> fotos) {
		this.fotos = fotos;
	}

	public String getRefFloraIberica() {
		return refFloraIberica;
	}

	public void setRefFloraIberica(String refFloraIberica) {
		this.refFloraIberica = refFloraIberica;
	}

}
