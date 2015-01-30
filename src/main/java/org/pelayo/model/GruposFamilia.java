package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the gruposfam database table.
 * 
 */
@Entity
@Table(name="Gruposfam")
@NamedQuery(name="GruposFamilia.findAll", query="SELECT g FROM GruposFamilia g")
public class GruposFamilia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nombreGrupo;

	@Lob
	private String comentario;

	private Integer  hayClave;

	private Integer  identEsp;

	private Byte marca1;

	private Byte marca2;

	private String nombreClGrupo;

	private Byte seleccionado;

	private Integer  seleccionar;

	public GruposFamilia() {
	}

	public String getNombreGrupo() {
		return this.nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Integer  getHayClave() {
		return this.hayClave;
	}

	public void setHayClave(Integer  hayClave) {
		this.hayClave = hayClave;
	}

	public Integer  getIdentEsp() {
		return this.identEsp;
	}

	public void setIdentEsp(Integer  identEsp) {
		this.identEsp = identEsp;
	}

	public Byte getMarca1() {
		return this.marca1;
	}

	public void setMarca1(Byte marca1) {
		this.marca1 = marca1;
	}

	public Byte getMarca2() {
		return this.marca2;
	}

	public void setMarca2(Byte marca2) {
		this.marca2 = marca2;
	}

	public String getNombreClGrupo() {
		return this.nombreClGrupo;
	}

	public void setNombreClGrupo(String nombreClGrupo) {
		this.nombreClGrupo = nombreClGrupo;
	}

	public Byte getSeleccionado() {
		return this.seleccionado;
	}

	public void setSeleccionado(Byte seleccionado) {
		this.seleccionado = seleccionado;
	}

	public Integer  getSeleccionar() {
		return this.seleccionar;
	}

	public void setSeleccionar(Integer  seleccionar) {
		this.seleccionar = seleccionar;
	}

}