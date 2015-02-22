package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The persistent class for the sectores database table.
 * 
 */
@Entity
@Table(name = "fitotipos")
@NamedQuery(name = "FitoTipo.findAll", query = "SELECT s FROM FitoTipo s")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etiq")
public class FitoTipo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String abrev;

	private String tipo;

	public FitoTipo() {
	}

	public String getAbrev() {
		return abrev;
	}

	public void setAbrev(String abrev) {
		this.abrev = abrev;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}