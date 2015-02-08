package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;


/**
 * The persistent class for the familias database table.
 * 
 */
@Entity
@Table(name="familias")
@NamedQuery(name="Familia.findAll", query="SELECT f FROM Familia f")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="nombreFam")
public class Familia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nombreFam;

	private Integer  flora;

	private String grupoFam;

	private Integer  hayClave;

	private Integer  identEsp;

	private Integer  idJaca;

	@Lob
	private String info;

	private Byte marca1;

	private Byte marca2;

	private String refFloraIberica;

	private Byte seleccionado;

	private Integer  seleccionar;

	//bi-directional many-to-one association to Género
	@OneToMany(mappedBy="familiaBean")
	private List<Genero> generos;

	public Familia() {
	}

	public String getNombreFam() {
		return this.nombreFam;
	}

	public void setNombreFam(String nombreFam) {
		this.nombreFam = nombreFam;
	}

	public Integer  getFlora() {
		return this.flora;
	}

	public void setFlora(Integer  flora) {
		this.flora = flora;
	}

	public String getGrupoFam() {
		return this.grupoFam;
	}

	public void setGrupoFam(String grupoFam) {
		this.grupoFam = grupoFam;
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

	public Integer  getIdJaca() {
		return this.idJaca;
	}

	public void setIdJaca(Integer  idJaca) {
		this.idJaca = idJaca;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public String getRefFloraIberica() {
		return this.refFloraIberica;
	}

	public void setRefFloraIberica(String refFloraIberica) {
		this.refFloraIberica = refFloraIberica;
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

	public List<Genero> getGeneros() {
		return this.generos;
	}

	public void setGeneros(List<Genero> géneros) {
		this.generos = géneros;
	}

	public Genero addGénero(Genero género) {
		getGeneros().add(género);
		género.setFamiliaBean(this);

		return género;
	}

	public Genero removeGénero(Genero género) {
		getGeneros().remove(género);
		género.setFamiliaBean(null);

		return género;
	}

}