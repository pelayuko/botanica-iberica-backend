package org.pelayo.dev.flickr.model;

import java.io.File;

import com.flickr4java.flickr.Flickr;

public class AuthorizedCommandModel {

	// Flickr flickr, File authsDir, String username

	private Flickr flickr;
	private String username;

	public static AuthorizedCommandModel mk() {
		return new AuthorizedCommandModel();
	}

	public Flickr getFlickr() {
		return flickr;
	}

	public void setFlickr(Flickr flickr) {
		this.flickr = flickr;
	}

	public AuthorizedCommandModel withFlickr(Flickr flickr) {
		this.flickr = flickr;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public AuthorizedCommandModel withUsername(String username) {
		this.username = username;
		return this;
	}
}
