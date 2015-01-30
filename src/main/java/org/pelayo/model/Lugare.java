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
 * The persistent class for the lugares database table.
 * 
 */
@Entity
@Table(name="lugares")
@NamedQuery(name="Lugare.findAll", query="SELECT l FROM Lugare l")
public class Lugare implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Integer altMax;

	private Integer altmin;

	@Lob
	private String descripción;

	private String nombre;

	//bi-directional many-to-one association to Localidentif
	@JsonIgnore
	@OneToMany(mappedBy="lugare", fetch=FetchType.EAGER)
	private List<Localidentif> localidentifs;

	public Lugare() {
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

	public List<Localidentif> getLocalidentifs() {
		return this.localidentifs;
	}

	public void setLocalidentifs(List<Localidentif> localidentifs) {
		this.localidentifs = localidentifs;
	}

	public Localidentif addLocalidentif(Localidentif localidentif) {
		getLocalidentifs().add(localidentif);
		localidentif.setLugare(this);

		return localidentif;
	}

	public Localidentif removeLocalidentif(Localidentif localidentif) {
		getLocalidentifs().remove(localidentif);
		localidentif.setLugare(null);

		return localidentif;
	}

}