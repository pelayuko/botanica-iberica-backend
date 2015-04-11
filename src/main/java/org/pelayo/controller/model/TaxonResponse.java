package org.pelayo.controller.model;

public class TaxonResponse {

	private String nombre;

	private String genero;

	private String familia;

	public TaxonResponse(String nombre) {
		this.nombre = nombre;
	}

	public TaxonResponse(String nombre, String genero) {
		this.nombre = nombre;
		this.genero = genero;
	}

	public TaxonResponse(String nombre, String genero, String familia) {
		this(nombre, genero);
		this.familia = familia;
	}

	public String getFamilia() {
		return familia;
	}

	public void setfamilia(String familia) {
		this.familia = familia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}
}
