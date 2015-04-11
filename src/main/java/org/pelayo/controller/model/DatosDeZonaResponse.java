package org.pelayo.controller.model;

import java.util.List;

public class DatosDeZonaResponse {

	private String nombreZona;

	private List<TaxonResponse> especies;

	private List<FotoResponse> fotos;

	private String descripcion;

	public String getNombreZona() {
		return nombreZona;
	}

	public void setNombreZona(String nombreZona) {
		this.nombreZona = nombreZona;
	}

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
