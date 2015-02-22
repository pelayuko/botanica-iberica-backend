package org.pelayo.dev.flickr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConfigurationProperties(locations = "classpath:personal.properties", ignoreUnknownFields = false, prefix = "flickr.api")
public class FlickrProps {

	private String key;
	private String secret;
	private String username;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void validate() {
		if (StringUtils.isEmpty(key)) {
			throw new RuntimeException("key is must be provided");
		}

		if (StringUtils.isEmpty(secret)) {
			throw new RuntimeException("secret must be provided");
		}

		if (StringUtils.isEmpty(username)) {
			throw new RuntimeException("username must be provided");
		}

	}

}
