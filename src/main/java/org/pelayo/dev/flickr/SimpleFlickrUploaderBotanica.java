package org.pelayo.dev.flickr;

import org.apache.log4j.Logger;
import org.pelayo.dev.flickr.commands.SimpleUploadPhotoCommand;
import org.pelayo.dev.flickr.config.FlickrProps;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.SimpleUploadPhotoModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;

@Configuration
@ComponentScan("org.pelayo")
@EnableAutoConfiguration
public class SimpleFlickrUploaderBotanica {

	private static final Logger log = Logger.getLogger(SimpleFlickrUploaderBotanica.class);

	private FlickrProps props;

	public static void main(String[] args) {

		if (args.length < 2) {
			Usage();
			System.exit(1);
		}

		ApplicationContext ctx = SpringApplication.run(SimpleFlickrUploaderBotanica.class, args);
		SimpleFlickrUploaderBotanica flickrUploader = new SimpleFlickrUploaderBotanica();
		flickrUploader.props = ctx.getBean(FlickrProps.class);
		if (flickrUploader.props == null) {
			throw new RuntimeException("Props is null!!");
		}
		flickrUploader.props.validate();

		try {

			String op = args[0];
			String photoId = args[1];

			long top = System.currentTimeMillis();

			SimpleUploadPhotoModel upModel = SimpleUploadPhotoModel.mk();
			upModel.withPhotoId(photoId);

			AuthorizedCommandModel authModel = flickrUploader.newAuthorizedCommandModel();
			switch (op) {
			case "-f":
				upModel.withMode(SimpleUploadPhotoModel.Mode.FLOWER);
				(new SimpleUploadPhotoCommand(ctx, authModel, flickrUploader.props)).execute(upModel);
				break;
			case "-l":
				upModel.withMode(SimpleUploadPhotoModel.Mode.LANDSCAPE);
				(new SimpleUploadPhotoCommand(ctx, authModel, flickrUploader.props)).execute(upModel);
				break;
			default:
				Usage();
				System.exit(1);

			}

			log.info("Took " + (System.currentTimeMillis() - top) / 1000 / 60 + " minutes");

			System.exit(0);
		} catch (Exception e) {

			log.error("Error executing Flickr uploader", e);

			System.exit(1);
		}

	}

	private AuthorizedCommandModel newAuthorizedCommandModel() {
		return AuthorizedCommandModel.mk() //
				.withFlickr(new Flickr(props.getKey(), props.getSecret(), new REST())) //
				.withUsername(props.getUsername());
	}

	private static void Usage() {
		System.out.println("Usage: java " + SimpleFlickrUploaderBotanica.class.getName() + "  [ -f | -l ] photo_id ");
		System.out.println("	f -> Flower photo ");
		System.out.println("	l -> Landscape photo ");
		System.out.println("	photo_id -> Id of photo to be uploaded ");

	}
}
