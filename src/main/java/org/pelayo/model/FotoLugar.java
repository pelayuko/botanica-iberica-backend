package org.pelayo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The persistent class for the fotoslugares database table.
 * 
 */
@Entity
@Table(name = "fotoslugares")
@NamedQuery(name = "Fotoslugare.findAll", query = "SELECT f FROM FotoLugar f")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idIdent")
public class FotoLugar implements IFoto, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer idIdent;

	@Lob
	private String comentario;

	private String coord;

	private Byte falta;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private String fichero;

	// bi-directional many-to-one association to Zona
	@ManyToOne
	@JoinColumn(name = "Sector")
	private Sector sector;

	// bi-directional many-to-one association to Ficherosfoto
	// @JsonIgnore
	@ManyToOne
	@JoinColumn(name = "IdFichero")
	private FicherosFoto ficherosfoto;

	// bi-directional many-to-one association to Zona
	// @JsonIgnore
	@ManyToOne
	@JoinColumn(name = "Zona")
	private Zona zona;

	public FotoLugar() {
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

	public String getCoord() {
		return this.coord;
	}

	public void setCoord(String coord) {
		this.coord = coord;
	}

	public Byte getFalta() {
		return this.falta;
	}

	public void setFalta(Byte falta) {
		this.falta = falta;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getFichero() {
		return this.fichero;
	}

	public void setFichero(String fichero) {
		this.fichero = fichero;
	}

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public FicherosFoto getFicherosfoto() {
		return this.ficherosfoto;
	}

	public void setFicherosfoto(FicherosFoto ficherosfoto) {
		this.ficherosfoto = ficherosfoto;
	}

	public Zona getZona() {
		return this.zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	@Override
	public String getSectorName() {
		return sector.getDenom();
	}

}