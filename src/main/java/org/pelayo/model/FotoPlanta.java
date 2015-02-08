package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * The persistent class for the fotosplantas database table.
 * 
 */
@Entity
@Table(name="fotosplantas")
@NamedQuery(name="Fotosplanta.findAll", query="SELECT f FROM FotoPlanta f")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="idIdent")
public class FotoPlanta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer idIdent;

	@Lob
	private String comentario;

	private Byte falta;

	private String fichero;

	//bi-directional many-to-one association to Cita
	// @JsonIgnore
	@ManyToOne
	@JoinColumn(name="Cita")
	private Cita citaBean;

	//bi-directional many-to-one association to Ficherosfoto
	@ManyToOne
	@JoinColumn(name="IdFichero")
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

	public Cita getCitaBean() {
		return this.citaBean;
	}

	public void setCitaBean(Cita citaBean) {
		this.citaBean = citaBean;
	}

	public FicherosFoto getFicherosfoto() {
		return this.ficherosfoto;
	}

	public void setFicherosfoto(FicherosFoto ficherosfoto) {
		this.ficherosfoto = ficherosfoto;
	}

}