package org.pelayo.controller.model;

import java.util.List;

public class DatosDeUTMResponse {
	
	private String UTM;

	private String elSector;

	private List<String> lasZonas;

	private List<TaxonResponse> especies;

	private List<FotoResponse> fotos;

	private String comentario;

	public List<TaxonResponse> getEspecies() {
		return especies;
	}

	public void setEspecies(List<TaxonResponse> especies) {
		this.especies = especies;
	}

	public List<FotoResponse> getFotos() {
		return fotos;
	}

	public void setFotos(List<FotoResponse> fotos) {
		this.fotos = fotos;
	}

	public String getUTM() {
		return UTM;
	}

	public void setUTM(String uTM) {
		UTM = uTM;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getElSector() {
		return elSector;
	}

	public void setElSector(String elSector) {
		this.elSector = elSector;
	}

	public List<String> getLasZonas() {
		return lasZonas;
	}

	public void setLasZonas(List<String> lasZonas) {
		this.lasZonas = lasZonas;
	}


}
