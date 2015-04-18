package org.pelayo.util;

import java.net.MalformedURLException;
import java.net.URL;

public class FlickrUtil {

	final static String DEFAULT_SIZE = PhotoSize.MEDIUM_500.toString();

	/*
	 * 
	 * 
	 * s small square 75x75 //
	 * q large square 150x150 //
	 * t thumbnail, 100 on longest side  //
	 * m small, 240 on longest side  //
	 * n small, 320 on longest side  //
	 * - medium, 500 on longest side  //
	 * z medium 640, 640 on longest side  //
	 * c medium 800, 800 on longest side†  //
	 * b large, 1024 on longest side*  //
	 * h large 1600, 1600 on longest side† // 
	 * k large 2048, 2048 on longest side† o original image,
	 * either a jpg, gif or png, depending on source format
	 */
	public enum PhotoSize {
		SMALL_SQUARE_75, SMALL_240, THUMBNAIL, MEDIUM_500, MEDIUM_800, LARGE_1024, LARGE_2048
	}
	
	public static URL buildUrl(PhotoSize size, String url) throws MalformedURLException {
		String base = url.substring(0, url.lastIndexOf("."));

		if (size != null) {
			switch (size) {
			case SMALL_SQUARE_75:
				base += "_s";
				break;
			case SMALL_240:
				base += "_m";
				break;
			case THUMBNAIL:
				base += "_t";
				break;
			case MEDIUM_500:
				base += "";
				break;
			case MEDIUM_800:
				base += "_c";
				break;
			case LARGE_1024:
				base += "_b";
				break;
			case LARGE_2048:
				base += "_k";
				break;
			default:
				break;
			}
		}

		return new URL(base + url.substring(url.lastIndexOf(".")));
	}
}
