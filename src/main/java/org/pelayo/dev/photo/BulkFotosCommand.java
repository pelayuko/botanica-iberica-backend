package org.pelayo.dev.photo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.pelayo.dao.FicherosFotoRepository;
import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.dev.flickr.example.UploadPhoto;
import org.pelayo.model.FicherosFoto;
import org.pelayo.model.IFoto;
import org.pelayo.storage.config.FotoFloraConfiguration;
import org.pelayo.storage.config.FotoPaisajesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
@EnableAutoConfiguration
@ComponentScan("org.pelayo")
public class BulkFotosCommand {

	static Integer count = 0;
	static Integer countNoExist = 0;
	static List<String> listMissingPath = new ArrayList<String>();

	private static final Logger logger = Logger.getLogger(UploadPhoto.class);

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(BulkFotosCommand.class, args);

		logger.info("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			logger.info(beanName);
		}

		try {
			BulkFotosCommand command = new BulkFotosCommand();

			command.createFotos(ctx, ctx.getBean(FotosLugaresRepository.class),
					ctx.getBean(FotoPaisajesConfiguration.class).getPath());
			command.createFotos(ctx, ctx.getBean(FotosPlantasRepository.class),
					ctx.getBean(FotoFloraConfiguration.class).getPath());

			logger.info("NO EXISTEN " + countNoExist);
			for (String p : listMissingPath) {
				logger.info("\t " + p);
			}

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}

	}

	public <T extends IFoto> void createFotos(ApplicationContext ctx, JpaRepository<T, Long> repo,
			String basePath) {
		List<T> fotosConSector = repo.findAll();

		for (T sectorProvider : fotosConSector) {
			String path = sectorProvider.getSectorName() + "/" + sectorProvider.getFichero() + ".jpg";

			String fullPath = basePath + "/" + path;
			File f = new File(fullPath);
			if (!f.exists()) {
				countNoExist++;
				listMissingPath.add(fullPath);
				logger.error("NO EXISTE " + fullPath);
			} else {
				logger.info(++count + " EXISTE " + fullPath);
				createFicheroFoto(ctx, repo, sectorProvider, path);
			}
		}

	}

	public <T extends IFoto> void createFicheroFoto(ApplicationContext ctx, JpaRepository<T, Long> repo,
			T sectorProvider, String path) {
		FicherosFoto ficheroFoto = new FicherosFoto();
		ficheroFoto.setId(UUID.randomUUID().toString());
		ficheroFoto.setPath(path);
		// ficheroFoto.addFotoslugare(fotoLugar);
		ctx.getBean(FicherosFotoRepository.class).save(ficheroFoto);

		sectorProvider.setFicherosfoto(ficheroFoto);
		repo.save(sectorProvider);
	}

}
