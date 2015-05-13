package org.pelayo.controller.model;

public class TaxonResponse {

	private String nombre; // nombre aceptado

	private String alternombre; // nombre común, sinónimo, etc

	private String genero;

	private String familia;
	private String fitotipo;
	private String fitosubtipo;
	private String colorflor;

	private FotoResponse foto;

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

	public TaxonResponse(String nombre, String nombrealt, String genero, String familia, FotoResponse foto) {
		this(nombre, nombrealt, genero, familia);
		this.foto = foto;
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

	public FotoResponse getFoto() {
		return foto;
	}

	public void setFoto(FotoResponse foto) {
		this.foto = foto;
	}

	public String getFitotipo() {
		return fitotipo;
	}

	public void setFitotipo(String fitotipo) {
		this.fitotipo = fitotipo;
	}

	public String getFitosubtipo() {
		return fitosubtipo;
	}

	public void setFitosubtipo(String fitosubtipo) {
		this.fitosubtipo = fitosubtipo;
	}

	public String getColorflor() {
		return colorflor;
	}

	public void setColorflor(String colorflor) {
		this.colorflor = colorflor;
	}
}
