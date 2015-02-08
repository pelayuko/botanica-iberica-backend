package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

/**
 * The persistent class for the nomespec database table.
 * 
 */
@Entity
@Table(name = "Nomespec")
@NamedQuery(name = "NombreEspecie.findAll", query = "SELECT n FROM NombreEspecie n")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="ident")
public class NombreEspecie implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer ident;

	private String aut1;

	private String aut2;

	private String nombreGen;

	private String restrictEsp;

	private String restrSubesp;

	// bi-directional many-to-one association to Especy
	// @JsonIgnore
	@OneToMany(mappedBy = "nomespec")
	private List<Especie> especies;

	public NombreEspecie() {
	}

	public Integer getIdent() {
		return this.ident;
	}

	public void setIdent(Integer ident) {
		this.ident = ident;
	}

	public String getAut1() {
		return this.aut1;
	}

	public void setAut1(String aut1) {
		this.aut1 = aut1;
	}

	public String getAut2() {
		return this.aut2;
	}

	public void setAut2(String aut2) {
		this.aut2 = aut2;
	}

	public String getNombreGen() {
		return this.nombreGen;
	}

	public void setNombreGen(String nombreGen) {
		this.nombreGen = nombreGen;
	}

	public String getRestrictEsp() {
		return this.restrictEsp;
	}

	public void setRestrictEsp(String restrictEsp) {
		this.restrictEsp = restrictEsp;
	}

	public String getRestrSubesp() {
		return this.restrSubesp;
	}

	public void setRestrSubesp(String restrSubesp) {
		this.restrSubesp = restrSubesp;
	}

	public List<Especie> getEspecies() {
		return this.especies;
	}

	public void setEspecies(List<Especie> especies) {
		this.especies = especies;
	}

	public Especie addEspecy(Especie especy) {
		getEspecies().add(especy);
		especy.setNomespec(this);

		return especy;
	}

	public Especie removeEspecy(Especie especy) {
		getEspecies().remove(especy);
		especy.setNomespec(null);

		return especy;
	}

}