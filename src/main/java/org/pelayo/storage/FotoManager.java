package org.pelayo.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.pelayo.storage.config.FotoFloraConfiguration;
import org.pelayo.storage.config.FotoPaisajesConfiguration;
import org.pelayo.util.FileHelper;
import org.pelayo.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FotoManager {

	@Autowired
	private FotoFloraConfiguration fotoFloraConfiguration;

	@Autowired
	private FotoPaisajesConfiguration fotoPaisajesConfiguration;

	public Foto getRandomFotoPaisaje() {
		return getRandomFoto(fotoPaisajesConfiguration.getPath());
	}

	public Foto getRandomFotoFlora() {
		return getRandomFoto(fotoFloraConfiguration.getPath());
	}

	public Foto getRandomFoto(String path) {
		Foto foto = new Foto();

		File file = getRandomFile(path);

		InputStream is = null;
		try {
			// FIXME: fix this hardcoded content type!!
//			is = ImageUtil.createThumbnail(file, "image/jpeg");
			is = ImageUtil.createRegularImage(file, "image/jpeg");
			foto.setContent(IOUtils.toByteArray(is));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}

		foto.setName(file.getName());
		foto.setLocation("file://" + file.getAbsolutePath());

		return foto;
	}

	public File getRandomFile(String path) {
		return FileHelper.randomFileInHierarchy(path);
	}
}
