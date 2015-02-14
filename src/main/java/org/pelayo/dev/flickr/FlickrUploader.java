package org.pelayo.dev.flickr;

import java.util.Collection;
import java.util.Collections;

import org.pelayo.dao.FotosLugaresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.test.TestInterface;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class FlickrUploader {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(FlickrUploader.class, args);

		try {
			FlickrUploader command = new FlickrUploader();
			
			FlickrProps props = ctx.getBean(FlickrProps.class);

			if (props == null) {
				throw new RuntimeException("Props is null!!");
			}

			String key = props.getKey();
			if (key == null) {
				throw new RuntimeException("Props is null!!");
			}

			String secret = props.getSecret();
			if (secret == null) {
				throw new RuntimeException("Props is null!!");
			}

			System.out.println("API key is " + key + " and secret " + secret);
			
			command.echo(key, secret);
//			UploadPhoto uploadPhoto = new UploadPhoto(key, nsid, secret, authsDir, username);
			// uploadPhoto.main(args);
			
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println("Error " + e.getMessage());
			System.exit(-1);
		}

	}

	public void echo(String key, String secret) throws FlickrException {
		Flickr f = new Flickr(key, secret, new REST());
		TestInterface testInterface = f.getTestInterface();
		Collection results = testInterface.echo(Collections.EMPTY_MAP);
	}

}
