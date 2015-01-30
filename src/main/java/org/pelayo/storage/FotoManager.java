package org.pelayo.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.pelayo.storage.config.FotoFloraConfiguration;
import org.pelayo.storage.config.FotoPaisajesConfiguration;
import org.pelayo.util.FileHelper;
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

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			foto.setContent(IOUtils.toByteArray(fis));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(fis);
		}

		foto.setName(file.getName());
		foto.setLocation("file://" + file.getAbsolutePath());

		return foto;
	}

	public File getRandomFile(String path) {
		return FileHelper.randomFileInHierarchy(path);
	}
}
