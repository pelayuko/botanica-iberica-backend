package org.pelayo.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sectores database table.
 * 
 */
@Entity
@Table(name="sectores")
@NamedQuery(name="Sector.findAll", query="SELECT s FROM Sector s")
public class Sector implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String etiq;

	private String denom;

	@Lob
	private String descrip;

	public Sector() {
	}

	public String getEtiq() {
		return this.etiq;
	}

	public void setEtiq(String etiq) {
		this.etiq = etiq;
	}

	public String getDenom() {
		return this.denom;
	}

	public void setDenom(String denom) {
		this.denom = denom;
	}

	public String getDescrip() {
		return this.descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

}