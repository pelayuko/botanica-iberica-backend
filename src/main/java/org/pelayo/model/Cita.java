package org.pelayo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The persistent class for the citas database table.
 * 
 */
@Entity
@Table(name = "citas")
@NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Cita implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Integer altitud;

	@Lob
	private String comentario;

	private String coord;

	private Integer especie;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private Byte invitada;

	private String lugar;

	// bi-directional many-to-one association to Zona
	@ManyToOne
	@JoinColumn(name = "Sector")
	private Sector sector;

	private Integer utmx;

	private Integer utmy;

	// bi-directional many-to-one association to Zona
	@ManyToOne
	@JoinColumn(name = "Zona")
	private Zona zona;

	// bi-directional many-to-one association to Fotosplanta
	@OneToMany(mappedBy = "cita")
	private List<FotoPlanta> fotosplantas;

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

	public Integer getEspecie() {
		return this.especie;
	}

	public void setEspecie(Integer especie) {
		this.especie = especie;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
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

	public Zona getZona() {
		return this.zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public List<FotoPlanta> getFotosplantas() {
		return this.fotosplantas;
	}

	public void setFotosplantas(List<FotoPlanta> fotosplantas) {
		this.fotosplantas = fotosplantas;
	}

	public FotoPlanta addFotosplanta(FotoPlanta fotosplanta) {
		getFotosplantas().add(fotosplanta);
		fotosplanta.setCita(this);

		return fotosplanta;
	}

	public FotoPlanta removeFotosplanta(FotoPlanta fotosplanta) {
		getFotosplantas().remove(fotosplanta);
		fotosplanta.setCita(null);

		return fotosplanta;
	}

}