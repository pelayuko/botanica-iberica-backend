package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * The persistent class for the géneros database table.
 * 
 */
@Entity
@Table(name="géneros")
@NamedQuery(name="Género.findAll", query="SELECT g FROM Genero g")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="nombreGen")
public class Genero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nombreGen;

	@Lob
	private String comentario;

	private Integer  idDilInicial;

	private Integer  identGen;

	private Byte marca1;

	private Byte marca2;

	private Integer  NEspCult;

	private Integer  NEspMax;

	private Integer  NEspMin;

	private Integer  NEspProb;

	private String nomComun;

	private String refFloraIberica;

	private Byte seleccionado;

	//bi-directional many-to-one association to Familia
	@ManyToOne
	@JoinColumn(name="Familia")
	// @JsonIgnore
	private Familia familia;

	public Genero() {
	}

	public String getNombreGen() {
		return this.nombreGen;
	}

	public void setNombreGen(String nombreGen) {
		this.nombreGen = nombreGen;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Integer  getIdDilInicial() {
		return this.idDilInicial;
	}

	public void setIdDilInicial(Integer  idDilInicial) {
		this.idDilInicial = idDilInicial;
	}

	public Integer  getIdentGen() {
		return this.identGen;
	}

	public void setIdentGen(Integer  identGen) {
		this.identGen = identGen;
	}

	public Byte getMarca1() {
		return this.marca1;
	}

	public void setMarca1(Byte marca1) {
		this.marca1 = marca1;
	}

	public Byte getMarca2() {
		return this.marca2;
	}

	public void setMarca2(Byte marca2) {
		this.marca2 = marca2;
	}

	public Integer  getNEspCult() {
		return this.NEspCult;
	}

	public void setNEspCult(Integer  NEspCult) {
		this.NEspCult = NEspCult;
	}

	public Integer  getNEspMax() {
		return this.NEspMax;
	}

	public void setNEspMax(Integer  NEspMax) {
		this.NEspMax = NEspMax;
	}

	public Integer  getNEspMin() {
		return this.NEspMin;
	}

	public void setNEspMin(Integer  NEspMin) {
		this.NEspMin = NEspMin;
	}

	public Integer  getNEspProb() {
		return this.NEspProb;
	}

	public void setNEspProb(Integer  NEspProb) {
		this.NEspProb = NEspProb;
	}

	public String getNomComun() {
		return this.nomComun;
	}

	public void setNomComun(String nomComun) {
		this.nomComun = nomComun;
	}

	public String getRefFloraIberica() {
		return this.refFloraIberica;
	}

	public void setRefFloraIberica(String refFloraIberica) {
		this.refFloraIberica = refFloraIberica;
	}

	public Byte getSeleccionado() {
		return this.seleccionado;
	}

	public void setSeleccionado(Byte seleccionado) {
		this.seleccionado = seleccionado;
	}

	public Familia getFamilia() {
		return this.familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

}