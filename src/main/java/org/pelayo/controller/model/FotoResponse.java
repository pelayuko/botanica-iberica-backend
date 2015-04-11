package org.pelayo.controller.model;

public class FotoResponse {

	public String fichero;
	public String comentario;
	public String utm;
	public String thumbnail;
	public String original;
	public String descripcion;
		
	public FotoResponse(String fichero, String coment){
		this.fichero = fichero;
		this.comentario= coment;
		String base = fichero.substring(0, fichero.lastIndexOf("."));
		String ext = fichero.substring(fichero.lastIndexOf("."));
		this.thumbnail=  base + "_m" + ext;
		this.original= base + "_b" + ext;
	}
	
	public FotoResponse(String fichero, String coment, String utm){
		this(fichero, coment);
		this.utm = utm;
	}

	public FotoResponse(String fichero, String coment, String utm, String descrip){
		this(fichero, coment, utm);
		this.descripcion = descrip;
	}

	public String getfichero() {
		return fichero;
	}
	
	public String getthumbnail() {
		return thumbnail;
	}

	public String getoriginal() {
		return original;
	}

	public void setfichero(String fichero) {
		this.fichero = fichero;
	}
	
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
