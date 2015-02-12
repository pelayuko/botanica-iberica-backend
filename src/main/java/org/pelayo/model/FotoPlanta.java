package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The persistent class for the fotosplantas database table.
 * 
 */
@Entity
@Table(name = "fotosplantas")
@NamedQuery(name = "Fotosplanta.findAll", query = "SELECT f FROM FotoPlanta f")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idIdent")
public class FotoPlanta implements SectorProvider, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer idIdent;

	@Lob
	private String comentario;

	private Byte falta;

	private String fichero;

	// bi-directional many-to-one association to Cita
	// @JsonIgnore
	@ManyToOne
	@JoinColumn(name = "Cita")
	private Cita cita;

	// bi-directional many-to-one association to Ficherosfoto
	@ManyToOne
	@JoinColumn(name = "IdFichero")
	private FicherosFoto ficherosfoto;

	public FotoPlanta() {
	}

	public Integer getIdIdent() {
		return this.idIdent;
	}

	public void setIdIdent(Integer idIdent) {
		this.idIdent = idIdent;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Byte getFalta() {
		return this.falta;
	}

	public void setFalta(Byte falta) {
		this.falta = falta;
	}

	public String getFichero() {
		return this.fichero;
	}

	public void setFichero(String fichero) {
		this.fichero = fichero;
	}

	public Cita getCita() {
		return this.cita;
	}

	public void setCita(Cita cita) {
		this.cita = cita;
	}

	public FicherosFoto getFicherosfoto() {
		return this.ficherosfoto;
	}

	public void setFicherosfoto(FicherosFoto ficherosfoto) {
		this.ficherosfoto = ficherosfoto;
	}

	@Override
	public String getSectorName() {
		return cita.getSector().getDenom();
	}

}