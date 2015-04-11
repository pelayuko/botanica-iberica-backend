package org.pelayo.controller.model;

public class TaxonResponse {

	private String nombre;

	private String taxon;

	private String familia;

	public TaxonResponse(String nombre) {
		this(nombre, nombre);
	}

	public TaxonResponse(String nombre, String taxon) {
		this.nombre = nombre;
		this.taxon = taxon;
	}

	public TaxonResponse(String nombre, String taxon, String familia) {
		this(nombre, taxon);
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

	public String getTaxon() {
		return taxon;
	}

	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}
}
