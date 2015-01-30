package org.pelayo.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the géneros database table.
 * 
 */
@Entity
@Table(name="géneros")
@NamedQuery(name="Género.findAll", query="SELECT g FROM Genero g")
public class Genero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nombreGen;

	@Lob
	private String comentario;

	private int idDilInicial;

	private int identGen;

	private byte marca1;

	private byte marca2;

	private int NEspCult;

	private int NEspMax;

	private int NEspMin;

	private int NEspProb;

	private String nomComun;

	private String refFloraIberica;

	private byte seleccionado;

	//bi-directional many-to-one association to Familia
	@ManyToOne
	@JoinColumn(name="Familia")
	private Familia familiaBean;

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

	public int getIdDilInicial() {
		return this.idDilInicial;
	}

	public void setIdDilInicial(int idDilInicial) {
		this.idDilInicial = idDilInicial;
	}

	public int getIdentGen() {
		return this.identGen;
	}

	public void setIdentGen(int identGen) {
		this.identGen = identGen;
	}

	public byte getMarca1() {
		return this.marca1;
	}

	public void setMarca1(byte marca1) {
		this.marca1 = marca1;
	}

	public byte getMarca2() {
		return this.marca2;
	}

	public void setMarca2(byte marca2) {
		this.marca2 = marca2;
	}

	public int getNEspCult() {
		return this.NEspCult;
	}

	public void setNEspCult(int NEspCult) {
		this.NEspCult = NEspCult;
	}

	public int getNEspMax() {
		return this.NEspMax;
	}

	public void setNEspMax(int NEspMax) {
		this.NEspMax = NEspMax;
	}

	public int getNEspMin() {
		return this.NEspMin;
	}

	public void setNEspMin(int NEspMin) {
		this.NEspMin = NEspMin;
	}

	public int getNEspProb() {
		return this.NEspProb;
	}

	public void setNEspProb(int NEspProb) {
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

	public byte getSeleccionado() {
		return this.seleccionado;
	}

	public void setSeleccionado(byte seleccionado) {
		this.seleccionado = seleccionado;
	}

	public Familia getFamiliaBean() {
		return this.familiaBean;
	}

	public void setFamiliaBean(Familia familiaBean) {
		this.familiaBean = familiaBean;
	}

}