package org.pelayo.controller.model;

import java.util.List;

public class DatosDeEspecieResponse {

	private String IdentTaxon;

	private String genero;

	private String familia;

	private TaxonResponse DatosTaxon;

	private List<CitaResponse> citas;

	private List<FotoResponse> fotos;

	private List<String> sinonimos;

	private List<String> comunes;

	private InfoTaxonResponse info;

	public String getIdentTaxon() {
		return IdentTaxon;
	}

	public void setIdentTaxon(String identTaxon) {
		IdentTaxon = identTaxon;
	}

	public TaxonResponse getDatosTaxon() {
		return DatosTaxon;
	}

	public void setDatosTaxon(TaxonResponse datosTaxon) {
		DatosTaxon = datosTaxon;
	}

	public List<CitaResponse> getCitas() {
		return citas;
	}

	public void setCitas(List<CitaResponse> citas) {
		this.citas = citas;
	}

	public List<FotoResponse> getFotos() {
		return fotos;
	}

	public void setFotos(List<FotoResponse> fotos) {
		this.fotos = fotos;
	}

	public List<String> getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(List<String> sinonimos) {
		this.sinonimos = sinonimos;
	}

	public List<String> getComunes() {
		return comunes;
	}

	public void setComunes(List<String> comunes) {
		this.comunes = comunes;
	}

	public InfoTaxonResponse getInfo() {
		return info;
	}

	public void setInfo(InfoTaxonResponse info) {
		this.info = info;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

}
