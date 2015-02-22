package org.pelayo.dev.flickr.commands.base;

import com.flickr4java.flickr.Flickr;

public abstract class AbstractBaseCommand {

	protected Flickr flickr;

	public AbstractBaseCommand(Flickr flickr) {
		this.flickr = flickr;
	}

	public Flickr getFlickr() {
		return flickr;
	}

}
