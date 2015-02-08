package org.pelayo.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ficherosfotos database table.
 * 
 */
@Entity
@Table(name="ficherosfotos")
@NamedQuery(name="Ficherosfoto.findAll", query="SELECT f FROM FicherosFoto f")
public class FicherosFoto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String flickrId;

	private String path;

	//bi-directional many-to-one association to Fotoslugare
	@OneToMany(mappedBy="ficherosfoto")
	private List<FotoLugar> fotoslugares;

	//bi-directional many-to-one association to Fotosplanta
	@OneToMany(mappedBy="ficherosfoto")
	private List<FotoPlanta> fotosplantas;

	public FicherosFoto() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlickrId() {
		return this.flickrId;
	}

	public void setFlickrId(String flickrId) {
		this.flickrId = flickrId;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<FotoLugar> getFotoslugares() {
		return this.fotoslugares;
	}

	public void setFotoslugares(List<FotoLugar> fotoslugares) {
		this.fotoslugares = fotoslugares;
	}

	public FotoLugar addFotoslugare(FotoLugar fotoslugare) {
		getFotoslugares().add(fotoslugare);
		fotoslugare.setFicherosfoto(this);

		return fotoslugare;
	}

	public FotoLugar removeFotoslugare(FotoLugar fotoslugare) {
		getFotoslugares().remove(fotoslugare);
		fotoslugare.setFicherosfoto(null);

		return fotoslugare;
	}

	public List<FotoPlanta> getFotosplantas() {
		return this.fotosplantas;
	}

	public void setFotosplantas(List<FotoPlanta> fotosplantas) {
		this.fotosplantas = fotosplantas;
	}

	public FotoPlanta addFotosplanta(FotoPlanta fotosplanta) {
		getFotosplantas().add(fotosplanta);
		fotosplanta.setFicherosfoto(this);

		return fotosplanta;
	}

	public FotoPlanta removeFotosplanta(FotoPlanta fotosplanta) {
		getFotosplantas().remove(fotosplanta);
		fotosplanta.setFicherosfoto(null);

		return fotosplanta;
	}

}