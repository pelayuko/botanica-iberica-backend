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

	private int hayClave;

	private int identEsp;

	private byte marca1;

	private byte marca2;

	private String nombreClGrupo;

	private byte seleccionado;

	private int seleccionar;

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

	public int getHayClave() {
		return this.hayClave;
	}

	public void setHayClave(int hayClave) {
		this.hayClave = hayClave;
	}

	public int getIdentEsp() {
		return this.identEsp;
	}

	public void setIdentEsp(int identEsp) {
		this.identEsp = identEsp;
	}

	public byte getMarca1() {
		return this.marca1;
	}

	public void setMarca1(byte marca1) {
		this.marca1 = marca1;
	}

	public byte getMarca2() {
		return this.marca2;
	}

	public void setMarca2(byte marca2) {
		this.marca2 = marca2;
	}

	public String getNombreClGrupo() {
		return this.nombreClGrupo;
	}

	public void setNombreClGrupo(String nombreClGrupo) {
		this.nombreClGrupo = nombreClGrupo;
	}

	public byte getSeleccionado() {
		return this.seleccionado;
	}

	public void setSeleccionado(byte seleccionado) {
		this.seleccionado = seleccionado;
	}

	public int getSeleccionar() {
		return this.seleccionar;
	}

	public void setSeleccionar(int seleccionar) {
		this.seleccionar = seleccionar;
	}

}