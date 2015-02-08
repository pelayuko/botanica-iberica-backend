package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;


/**
 * The persistent class for the zonas database table.
 * 
 */
@Entity
@Table(name="zonas")
@NamedQuery(name="Zona.findAll", query="SELECT z FROM Zona z")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Zona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Integer altMax;

	private Integer altmin;

	@Lob
	private String descripción;

	private String nombre;

	//bi-directional many-to-one association to Cita
	// @JsonIgnore
	@OneToMany(mappedBy="zona")
	private List<Cita> citas;

	//bi-directional many-to-one association to Fotoslugare
	// @JsonIgnore
	@OneToMany(mappedBy="zonaBean")
	private List<FotoLugar> fotoslugares;

	public Zona() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAltMax() {
		return this.altMax;
	}

	public void setAltMax(Integer altMax) {
		this.altMax = altMax;
	}

	public Integer getAltmin() {
		return this.altmin;
	}

	public void setAltmin(Integer altmin) {
		this.altmin = altmin;
	}

	public String getDescripción() {
		return this.descripción;
	}

	public void setDescripción(String descripción) {
		this.descripción = descripción;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Cita> getCitas() {
		return this.citas;
	}

	public void setCitas(List<Cita> citas) {
		this.citas = citas;
	}

	public Cita addCita(Cita cita) {
		getCitas().add(cita);
		cita.setZona(this);

		return cita;
	}

	public Cita removeCita(Cita cita) {
		getCitas().remove(cita);
		cita.setZona(null);

		return cita;
	}

	public List<FotoLugar> getFotoslugares() {
		return this.fotoslugares;
	}

	public void setFotoslugares(List<FotoLugar> fotoslugares) {
		this.fotoslugares = fotoslugares;
	}

	public FotoLugar addFotoslugare(FotoLugar fotoslugare) {
		getFotoslugares().add(fotoslugare);
		fotoslugare.setZonaBean(this);

		return fotoslugare;
	}

	public FotoLugar removeFotoslugare(FotoLugar fotoslugare) {
		getFotoslugares().remove(fotoslugare);
		fotoslugare.setZonaBean(null);

		return fotoslugare;
	}

}