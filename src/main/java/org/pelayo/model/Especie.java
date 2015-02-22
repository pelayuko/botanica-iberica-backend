package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * The persistent class for the especies database table.
 * 
 */
@Entity
@Table(name="especies")
@NamedQuery(name="Especies.findAll", query="SELECT e FROM Especie e")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="identEsp")
public class Especie implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer identEsp;

	private Byte aromatica;

	private Byte atribArb;

	private Byte atribCult;

	private String color;

	@Lob
	private String comentario;

	private Byte comestible;

	private String fitoSubtipo;

	private String fitoTipo;

	private Byte foranea;

	private String frecComarca;

	private String frecSoria;

	@ManyToOne
	@JoinColumn(name="Género")
	private Genero género;

	private String idJaca;

	private String idPirineos;

	private Byte introducida;

	private Byte marca_us1;

	private Byte marca_us2;

	private Byte marca1;

	private Byte marca2;

	private Byte medicinal;

	private Byte mesfinflor;

	private Byte mesiniflor;

	private String nota;

	private String presComarca;

	private Byte seleccionado;

	private Byte statusFotos;

	private Integer tammaxmax;

	private Integer tammaxnor;

	private Integer tamminmin;

	private Integer tamminnor;

	private Byte tóxica;

	//bi-directional many-to-one association to Nomespec
	@ManyToOne
	@JoinColumn(name="Nombre")
	private NombreEspecie nomespec;

	public Especie() {
	}

	public Integer getIdentEsp() {
		return this.identEsp;
	}

	public void setIdentEsp(Integer identEsp) {
		this.identEsp = identEsp;
	}

	public Byte getAromatica() {
		return this.aromatica;
	}

	public void setAromatica(Byte aromatica) {
		this.aromatica = aromatica;
	}

	public Byte getAtribArb() {
		return this.atribArb;
	}

	public void setAtribArb(Byte atribArb) {
		this.atribArb = atribArb;
	}

	public Byte getAtribCult() {
		return this.atribCult;
	}

	public void setAtribCult(Byte atribCult) {
		this.atribCult = atribCult;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Byte getComestible() {
		return this.comestible;
	}

	public void setComestible(Byte comestible) {
		this.comestible = comestible;
	}

	public String getFitoSubtipo() {
		return this.fitoSubtipo;
	}

	public void setFitoSubtipo(String fitoSubtipo) {
		this.fitoSubtipo = fitoSubtipo;
	}

	public String getFitoTipo() {
		return this.fitoTipo;
	}

	public void setFitoTipo(String fitoTipo) {
		this.fitoTipo = fitoTipo;
	}

	public Byte getForanea() {
		return this.foranea;
	}

	public void setForanea(Byte foranea) {
		this.foranea = foranea;
	}

	public String getFrecComarca() {
		return this.frecComarca;
	}

	public void setFrecComarca(String frecComarca) {
		this.frecComarca = frecComarca;
	}

	public String getFrecSoria() {
		return this.frecSoria;
	}

	public void setFrecSoria(String frecSoria) {
		this.frecSoria = frecSoria;
	}

	public Genero getGenero() {
		return this.género;
	}

	public void setGenero(Genero género) {
		this.género = género;
	}

	public String getIdJaca() {
		return this.idJaca;
	}

	public void setIdJaca(String idJaca) {
		this.idJaca = idJaca;
	}

	public String getIdPirineos() {
		return this.idPirineos;
	}

	public void setIdPirineos(String idPirineos) {
		this.idPirineos = idPirineos;
	}

	public Byte getIntroducida() {
		return this.introducida;
	}

	public void setIntroducida(Byte introducida) {
		this.introducida = introducida;
	}

	public Byte getMarca_us1() {
		return this.marca_us1;
	}

	public void setMarca_us1(Byte marca_us1) {
		this.marca_us1 = marca_us1;
	}

	public Byte getMarca_us2() {
		return this.marca_us2;
	}

	public void setMarca_us2(Byte marca_us2) {
		this.marca_us2 = marca_us2;
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

	public Byte getMedicinal() {
		return this.medicinal;
	}

	public void setMedicinal(Byte medicinal) {
		this.medicinal = medicinal;
	}

	public Byte getMesfinflor() {
		return this.mesfinflor;
	}

	public void setMesfinflor(Byte mesfinflor) {
		this.mesfinflor = mesfinflor;
	}

	public Byte getMesiniflor() {
		return this.mesiniflor;
	}

	public void setMesiniflor(Byte mesiniflor) {
		this.mesiniflor = mesiniflor;
	}

	public String getNota() {
		return this.nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getPresComarca() {
		return this.presComarca;
	}

	public void setPresComarca(String presComarca) {
		this.presComarca = presComarca;
	}

	public Byte getSeleccionado() {
		return this.seleccionado;
	}

	public void setSeleccionado(Byte seleccionado) {
		this.seleccionado = seleccionado;
	}

	public Byte getStatusFotos() {
		return this.statusFotos;
	}

	public void setStatusFotos(Byte statusFotos) {
		this.statusFotos = statusFotos;
	}

	public Integer getTammaxmax() {
		return this.tammaxmax;
	}

	public void setTammaxmax(Integer tammaxmax) {
		this.tammaxmax = tammaxmax;
	}

	public Integer getTammaxnor() {
		return this.tammaxnor;
	}

	public void setTammaxnor(Integer tammaxnor) {
		this.tammaxnor = tammaxnor;
	}

	public Integer getTamminmin() {
		return this.tamminmin;
	}

	public void setTamminmin(Integer tamminmin) {
		this.tamminmin = tamminmin;
	}

	public Integer getTamminnor() {
		return this.tamminnor;
	}

	public void setTamminnor(Integer tamminnor) {
		this.tamminnor = tamminnor;
	}

	public Byte getTóxica() {
		return this.tóxica;
	}

	public void setTóxica(Byte tóxica) {
		this.tóxica = tóxica;
	}

	public NombreEspecie getNomespec() {
		return this.nomespec;
	}

	public void setNomespec(NombreEspecie nomespec) {
		this.nomespec = nomespec;
	}

}