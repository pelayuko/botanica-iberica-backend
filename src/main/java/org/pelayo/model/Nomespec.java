package org.pelayo.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the nomespec database table.
 * 
 */
@Entity
@NamedQuery(name="Nomespec.findAll", query="SELECT n FROM Nomespec n")
public class Nomespec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer ident;

	private String aut1;

	private String aut2;

	private String nombreGen;

	private String restrictEsp;

	private String restrSubesp;

	//bi-directional many-to-one association to Especy
	@JsonIgnore
	@OneToMany(mappedBy="nomespec")
	private List<Especy> especies;

	public Nomespec() {
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

	public List<Especy> getEspecies() {
		return this.especies;
	}

	public void setEspecies(List<Especy> especies) {
		this.especies = especies;
	}

	public Especy addEspecy(Especy especy) {
		getEspecies().add(especy);
		especy.setNomespec(this);

		return especy;
	}

	public Especy removeEspecy(Especy especy) {
		getEspecies().remove(especy);
		especy.setNomespec(null);

		return especy;
	}

}