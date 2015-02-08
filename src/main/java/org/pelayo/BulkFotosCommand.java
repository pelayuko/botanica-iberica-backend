package org.pelayo;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.pelayo.dao.FicherosFotoRepository;
import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.dao.VincSectoresZonaRepository;
import org.pelayo.model.FicherosFoto;
import org.pelayo.model.FotoLugar;
import org.pelayo.model.VincSectoresZona;
import org.pelayo.storage.config.FotoFloraConfiguration;
import org.pelayo.storage.config.FotoPaisajesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class BulkFotosCommand {

	@Autowired
	FotosLugaresRepository fotosLugaresRepository;

	@Autowired
	FicherosFotoRepository ficherosFotoRepository;

	@Autowired
	VincSectoresZonaRepository vincSectoresZonaRepository;

	@Autowired
	private FotoFloraConfiguration fotoFloraConfiguration;

	@Autowired
	private FotoPaisajesConfiguration fotoPaisajesConfiguration;

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(BulkFotosCommand.class,
				args);

		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}

		(new BulkFotosCommand()).start(ctx);
	}

	public void start(ApplicationContext ctx) {
		List<FotoLugar> fotosLugares = ctx
				.getBean(FotosLugaresRepository.class).findAll();
		String basePath = ctx.getBean(FotoPaisajesConfiguration.class)
				.getPath() + "/";

		for (FotoLugar fotoLugar : fotosLugares) {
			List<VincSectoresZona> vincSectoresZonas = ctx.getBean(
					VincSectoresZonaRepository.class).findByZona(
					fotoLugar.getZonaBean());

			for (VincSectoresZona vsz : vincSectoresZonas) {
				String path = vsz.getSector().getDenom() + "/"
						+ fotoLugar.getFichero() + ".jpg";

				String fullPath = basePath + path;
				File f = new File(fullPath);
				if (f.exists()) {
					System.out.println("EXISTE " + fullPath);
					createFicheroFoto(ctx, fotoLugar, path);
				} else {
					System.out.println("NO EXISTE " + fullPath);
				}

			}

		}

	}

	public void createFicheroFoto(ApplicationContext ctx, FotoLugar fotoLugar,
			String path) {
		FicherosFoto ficheroFoto = new FicherosFoto();
		ficheroFoto.setId(UUID.randomUUID().toString());
		ficheroFoto.setPath(path);
		// ficheroFoto.addFotoslugare(fotoLugar);
		ctx.getBean(FicherosFotoRepository.class).save(ficheroFoto);

		fotoLugar.setFicherosfoto(ficheroFoto);
		ctx.getBean(FotosLugaresRepository.class).save(fotoLugar);
	}
}
