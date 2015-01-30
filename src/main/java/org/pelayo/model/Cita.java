package org.pelayo.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the localidentif database table.
 * 
 */
@Entity
@Table(name="Localidentif")
@NamedQuery(name="Citas.findAll", query="SELECT l FROM Cita l")
public class Cita implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Integer altitud;

	@Lob
	private String comentario;

	private String coord;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private String idZona;

	private Byte invitada;

	private String lugar;

	private Integer utmx;

	private Integer utmy;

	//bi-directional many-to-one association to Especy
	@ManyToOne
	@JoinColumn(name="Especie")
	private Especie especie;

	//bi-directional many-to-one association to Lugare
	@ManyToOne
	@JoinColumn(name="Sitio")
	private Lugares lugare;

	public Cita() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAltitud() {
		return this.altitud;
	}

	public void setAltitud(Integer altitud) {
		this.altitud = altitud;
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

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getIdZona() {
		return this.idZona;
	}

	public void setIdZona(String idZona) {
		this.idZona = idZona;
	}

	public Byte getInvitada() {
		return this.invitada;
	}

	public void setInvitada(Byte invitada) {
		this.invitada = invitada;
	}

	public String getLugar() {
		return this.lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Integer getUtmx() {
		return this.utmx;
	}

	public void setUtmx(Integer utmx) {
		this.utmx = utmx;
	}

	public Integer getUtmy() {
		return this.utmy;
	}

	public void setUtmy(Integer utmy) {
		this.utmy = utmy;
	}

	public Especie getEspecy() {
		return this.especie;
	}

	public void setEspecy(Especie especy) {
		this.especie = especy;
	}

	public Lugares getLugare() {
		return this.lugare;
	}

	public void setLugare(Lugares lugare) {
		this.lugare = lugare;
	}

}