package org.pelayo.dev.flickr.commands;

import java.util.Collection;
import java.util.Collections;

import org.pelayo.dev.flickr.commands.base.AbstractBaseCommand;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.test.TestInterface;

public class EchoCommand extends AbstractBaseCommand {

	public EchoCommand(Flickr flickr) {
		super(flickr);
	}

	public void execute() throws FlickrException {
		TestInterface testInterface = flickr.getTestInterface();
		Collection results = testInterface.echo(Collections.EMPTY_MAP);
	}
}
