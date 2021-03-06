package org.pelayo.controller.model;

import java.util.List;

public class DatosDeFamiliaResponse {

	private List<TaxonResponse> especies;

	private String familia;

	private String grupo;

	private String refFloraIberica;

	private List<FotoResponse> fotos;

	private List<String> sinonimos;

	private List<String> subfamilias;

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

	public List<FotoResponse> getFotos() {
		return fotos;
	}

	public void setFotos(List<FotoResponse> fotos) {
		this.fotos = fotos;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getRefFloraIberica() {
		return refFloraIberica;
	}

	public void setRefFloraIberica(String refFloraIberica) {
		this.refFloraIberica = refFloraIberica;
	}

	public List<String> getSinonimos() {
		return sinonimos;
	}

	public void setSinonimos(List<String> sinonimos) {
		this.sinonimos = sinonimos;
	}

	public List<String> getSubfamilias() {
		return subfamilias;
	}

	public void setSubfamilias(List<String> subfamilias) {
		this.subfamilias = subfamilias;
	}

}
