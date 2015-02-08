package org.pelayo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the zonas database table.
 * 
 */
@Entity
@Table(name = "zonas")
@NamedQuery(name = "Zonas.findAll", query = "SELECT l FROM Zonas l")
public class Zonas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Integer altMax;

	private Integer altmin;

	@Lob
	private String descripción;

	private String nombre;

	// bi-directional many-to-one association to Localidentif
	@JsonIgnore
	@OneToMany(mappedBy = "zona", fetch = FetchType.EAGER)
	private List<Cita> citas;

	public Zonas() {
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

	public Cita removeLocalidentif(Cita cita) {
		getCitas().remove(cita);
		cita.setZona(null);

		return cita;
	}

}