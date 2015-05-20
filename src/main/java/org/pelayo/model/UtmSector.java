package org.pelayo.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the utmsectores database table.
 * 
 */
@Entity
@Table(name="UtmSectores")
@NamedQuery(name="UtmSector.findAll", query="SELECT u FROM UtmSector u")
public class UtmSector implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String utm;

	private int conCita;

	private String sector;

	public UtmSector() {
	}

	public String getUtm() {
		return this.utm;
	}

	public void setUtm(String utm) {
		this.utm = utm;
	}

	public int getConCita() {
		return this.conCita;
	}

	public void setConCita(int conCita) {
		this.conCita = conCita;
	}

	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

}