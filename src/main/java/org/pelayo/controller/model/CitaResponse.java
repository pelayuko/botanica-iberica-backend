package org.pelayo.controller.model;

public class CitaResponse {

	public String utm1x1;
	public String sector;
	public String laZona;
	public String laFecha;
	public String elLugar;
	public String comentario;
		
	public CitaResponse(String utm1x1, String sector){
		this.utm1x1 = utm1x1;
		this.sector = sector;
	}

	public String getutm1x1() {
		return utm1x1;
	}

	public void setutm1x1(String utm1x1) {
		this.utm1x1 = utm1x1;
	}
	
	public String getsector() {
		return sector;
	}

	public void setsector(String sector) {
		this.sector = sector;
	}
	
	public String getlaZona() {
		return laZona;
	}

	public void setlaZona(String lazona) {
		this.laZona = lazona;
	}
	
	public String getelLugar() {
		return elLugar;
	}

	public void setelLugar(String lugar) {
		this.elLugar = lugar;
	}
}
