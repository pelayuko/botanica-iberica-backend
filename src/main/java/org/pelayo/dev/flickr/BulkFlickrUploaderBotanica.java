package org.pelayo.dev.flickr;

import org.apache.log4j.Logger;
import org.pelayo.dev.flickr.commands.DeleteAllPhotosCommand;
import org.pelayo.dev.flickr.commands.ProcessAllPhotosCommand;
import org.pelayo.dev.flickr.commands.ProcessAllPhotosCommand.Mode;
import org.pelayo.dev.flickr.commands.RegenerateAllAlbumsCommand;
import org.pelayo.dev.flickr.config.FlickrProps;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.VoidModel;
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
public class BulkFlickrUploaderBotanica {

	private static final Logger log = Logger.getLogger(BulkFlickrUploaderBotanica.class);

	private FlickrProps props;

	// @Autowired
	// private FotoPaisajesConfiguration fotoPaisajeConfiguration;

	public static void main(String[] args) {

		if (args.length < 1) {
			Usage();
			System.exit(1);
		}

		ApplicationContext ctx = SpringApplication.run(BulkFlickrUploaderBotanica.class, args);
		BulkFlickrUploaderBotanica flickrUploader = new BulkFlickrUploaderBotanica();
		flickrUploader.props = ctx.getBean(FlickrProps.class);
		if (flickrUploader.props == null) {
			throw new RuntimeException("Props is null!!");
		}
		flickrUploader.props.validate();

		try {

			String op = args[0];

			long top = System.currentTimeMillis();

			AuthorizedCommandModel authModel = flickrUploader.newAuthorizedCommandModel();
			switch (op) {
			case "-pt":
				(new ProcessAllPhotosCommand(ctx, authModel, flickrUploader.props, Mode.TAG)).execute(VoidModel.mk());
				break;
			case "-pn":
				(new ProcessAllPhotosCommand(ctx, authModel, flickrUploader.props, Mode.RENAME))
						.execute(VoidModel.mk());
				break;
			case "-pu":
				(new ProcessAllPhotosCommand(ctx, authModel, flickrUploader.props, Mode.UPLOAD))
						.execute(VoidModel.mk());
				break;
			case "-al":
				(new RegenerateAllAlbumsCommand(ctx, authModel)).execute(VoidModel.mk());
				break;
			case "-delete":
				(new DeleteAllPhotosCommand(ctx, authModel)).execute(VoidModel.mk());
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
		System.out.println("Usage: java " + BulkFlickrUploaderBotanica.class.getName()
				+ "  [ -pt | -pn | -pu | -al | -delete ]");
		System.out.println("	pt -> Rerocess tags, asumming photos are uploaded ");
		System.out.println("	pn -> Reprocess names, asumming photos are uploaded ");
		System.out.println("	pu -> Uploads photos that are not uploaded ");
		System.out.println("	al -> Regenerate all albums ");
		System.out.println("	delete -> Delete all photos ");

	}
}
