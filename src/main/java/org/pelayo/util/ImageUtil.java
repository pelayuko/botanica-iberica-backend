package org.pelayo.util;

import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_BRIGHTER;
import static org.imgscalr.Scalr.resize;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr.Method;

public class ImageUtil {

	private static final int THUMBNAIL_SIZE = 350;
	private static final int REGULAR_SIZE = 600;

	public static InputStream createThumbnail(File file, String contentType) {
//		return compress(file, contentType, Method.AUTOMATIC, THUMBNAIL_SIZE);
		return compress(file, contentType, Method.SPEED, THUMBNAIL_SIZE);
	}

	public static InputStream createRegularImage(File file, String contentType) {
		return compress(file, contentType, Method.BALANCED, REGULAR_SIZE);
	}

	private static InputStream compress(File file, String contentType, Method method, int size) {
		BufferedImage img;
		ByteArrayOutputStream os = null;
		try {
			img = ImageIO.read(file);

			// Create quickly, then smooth and brighten it.
			img = resize(img, method, size, OP_ANTIALIAS, OP_BRIGHTER);

			// Let's add a little border before we return result.
//			BufferedImage pad = pad(img, 2);

			os = new ByteArrayOutputStream();

			String format = contentType.substring(contentType.lastIndexOf("/") + 1);
			ImageIO.write(img, format, os);

			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
}
