package org.pelayo.dev.flickr.util;

public class MyStringUtils {

	public static String voidIfNull(String s) {
		if (s == null)
			return "";
		return s;
	}
}
