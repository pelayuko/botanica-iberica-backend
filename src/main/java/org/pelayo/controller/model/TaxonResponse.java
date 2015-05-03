package org.pelayo.controller.model;

public class TaxonResponse {

	private String nombre; // nombre aceptado

	private String alternombre; // nombre común, sinónimo, etc

	private String genero;

	private String familia;

	public TaxonResponse(String nombre, String nombrealt) {
		this.nombre = nombre;
		this.alternombre = nombrealt;
	}

	public TaxonResponse(String nombre, String nombrealt, String genero) {
		this(nombre, nombrealt);
		this.genero = genero;
	}

	public TaxonResponse(String nombre, String nombrealt, String genero, String familia) {
		this(nombre, nombrealt, genero);
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

	public String getAlternombre() {
		return alternombre;
	}

	public void setAlternombre(String alternombre) {
		this.alternombre = alternombre;
	}
}
