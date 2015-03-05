package org.pelayo.dev.flickr.util;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

public class FlickrHelper {

	public static final String ERROR = "ERROR";

	public static final String SUCCESS = "SUCCESS";
	
	public static final String PAISAJE_PREFIX = "Paisaje";

	public static final String PLANTA_PREFIX = "Plantas";

	private static final Logger logger = Logger.getLogger(FlickrHelper.class);

	private static final String[] photoSuffixes = { "jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff" };

	private static final String[] videoSuffixes = { "3gp", "3gp", "avi", "mov", "mp4", "mpg", "mpeg", "wmv", "ogg",
			"ogv", "m2v" };

	public static boolean isValidSuffix(String basefilename) {
		if (basefilename.lastIndexOf('.') <= 0) {
			return false;
		}
		String suffix = basefilename.substring(basefilename.lastIndexOf('.') + 1).toLowerCase();
		for (int i = 0; i < photoSuffixes.length; i++) {
			if (photoSuffixes[i].equals(suffix))
				return true;
		}
		for (int i = 0; i < videoSuffixes.length; i++) {
			if (videoSuffixes[i].equals(suffix))
				return true;
		}
		logger.debug(basefilename + " does not have a valid suffix, skipped.");
		return false;
	}

	public static String normalize(String input, boolean forzeReplaceSpaces) {
		byte[] fname = input.getBytes();
		byte[] bad = new byte[] { '\\', '/', '"', '*' };
		byte replace = '_';
		for (int i = 0; i < fname.length; i++) {
			for (byte element : bad) {
				if (fname[i] == element) {
					fname[i] = replace;
				}
			}
			if ((forzeReplaceSpaces) && fname[i] == ' ')
				fname[i] = '_';
		}
		return new String(fname);
	}

	static class UploadFilenameFilter implements FilenameFilter {

		// Following suffixes from flickr upload page. An App should have this
		// configurable,
		// for videos and photos separately.

		@Override
		public boolean accept(File dir, String name) {
			if (isValidSuffix(name))
				return true;
			else
				return false;
		}

	}
}
