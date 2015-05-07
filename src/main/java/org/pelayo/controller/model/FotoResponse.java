package org.pelayo.controller.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.pelayo.util.FlickrUtil;
import org.pelayo.util.FlickrUtil.PhotoSize;

public class FotoResponse {

	private String comentario;
	private String utm;
	private String zona;
	private final URL smallSquareURL;
	private final URL small240URL;
	private final URL thumbnailURL;
	private final URL mediumURL;
	private final URL originalURL;
	private String descripcion;

	public FotoResponse(String url, String coment) {
		this.comentario = coment;

		try {
			this.smallSquareURL = FlickrUtil.buildUrl(PhotoSize.SMALL_SQUARE_75, url);
			this.small240URL = FlickrUtil.buildUrl(PhotoSize.SMALL_240, url);
			this.thumbnailURL = FlickrUtil.buildUrl(PhotoSize.THUMBNAIL, url);
			this.mediumURL = FlickrUtil.buildUrl(PhotoSize.MEDIUM_800, url);
			this.originalURL = FlickrUtil.buildUrl(PhotoSize.LARGE_1024, url);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unable to build url!", e);
		}
	}

	public FotoResponse(String url, String coment, String utm) {
		this(url, coment);
		this.setUtm(utm);
	}

	public FotoResponse(String url, String coment, String utm, String descrip) {
		this(url, coment, utm);
		this.descripcion = descrip;
	}
	
	public FotoResponse(String url, String coment, String utm, String descrip, String zona) {
		this(url, coment, utm, descrip);
		this.setZona(zona);
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUtm() {
		return utm;
	}

	public void setUtm(String utm) {
		this.utm = utm;
	}

	public URL getThumbnailURL() {
		return thumbnailURL;
	}

	public URL getMediumURL() {
		return mediumURL;
	}

	public URL getOriginalURL() {
		return originalURL;
	}

	public URL getSmallSquareURL() {
		return smallSquareURL;
	}

	public URL getSmall240URL() {
		return small240URL;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}
}
