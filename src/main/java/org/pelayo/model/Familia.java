package org.pelayo.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the familias database table.
 * 
 */
@Entity
@Table(name="familias")
@NamedQuery(name="Familia.findAll", query="SELECT f FROM Familia f")
public class Familia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nombreFam;

	private int flora;

	private String grupoFam;

	private int hayClave;

	private int identEsp;

	private int idJaca;

	@Lob
	private String info;

	private byte marca1;

	private byte marca2;

	private String refFloraIberica;

	private byte seleccionado;

	private int seleccionar;

	//bi-directional many-to-one association to Género
	@OneToMany(mappedBy="familiaBean")
	private List<Genero> géneros;

	public Familia() {
	}

	public String getNombreFam() {
		return this.nombreFam;
	}

	public void setNombreFam(String nombreFam) {
		this.nombreFam = nombreFam;
	}

	public int getFlora() {
		return this.flora;
	}

	public void setFlora(int flora) {
		this.flora = flora;
	}

	public String getGrupoFam() {
		return this.grupoFam;
	}

	public void setGrupoFam(String grupoFam) {
		this.grupoFam = grupoFam;
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

	public int getIdJaca() {
		return this.idJaca;
	}

	public void setIdJaca(int idJaca) {
		this.idJaca = idJaca;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public String getRefFloraIberica() {
		return this.refFloraIberica;
	}

	public void setRefFloraIberica(String refFloraIberica) {
		this.refFloraIberica = refFloraIberica;
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

	public List<Genero> getGéneros() {
		return this.géneros;
	}

	public void setGéneros(List<Genero> géneros) {
		this.géneros = géneros;
	}

	public Genero addGénero(Genero género) {
		getGéneros().add(género);
		género.setFamiliaBean(this);

		return género;
	}

	public Genero removeGénero(Genero género) {
		getGéneros().remove(género);
		género.setFamiliaBean(null);

		return género;
	}

}