package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the vincsectoreszonas database table.
 * 
 */
@Entity
@Table(name = "vincsectoreszonas")
@NamedQuery(name = "VincSectoresZona.findAll", query = "SELECT v FROM VincSectoresZona v")
public class VincSectoresZona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idNexo;

	@Lob
	private String comentario;

	// bi-directional many-to-one association to Zona
	@ManyToOne
	@JoinColumn(name = "Sector")
	private Sector sector;

	// bi-directional many-to-one association to Zona
	@ManyToOne
	@JoinColumn(name = "Zona")
	private Zona zona;

	public VincSectoresZona() {
	}

	public int getIdNexo() {
		return this.idNexo;
	}

	public void setIdNexo(int idNexo) {
		this.idNexo = idNexo;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public Zona getZona() {
		return this.zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

}