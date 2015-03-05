package org.pelayo.dev.flickr.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.flickr4java.flickr.util.StringUtilities;

public class PhotoUploadModel implements IModel {

	private String fileName;
	private String name;
	private String albumName;
	private String description;
	private List<String> tags;
	private GeoDataModel geoData;

	public static PhotoUploadModel mk() {
		return new PhotoUploadModel();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public PhotoUploadModel withFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PhotoUploadModel withName(String name) {
		this.name = name;
		return this;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public PhotoUploadModel withAlbumName(String albumName) {
		this.albumName = albumName;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PhotoUploadModel withDescription(String description) {
		this.description = description;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public PhotoUploadModel withTag(String tag) {
		if (StringUtils.isEmpty(tag)) {
			return this;
		}

		if ("INDEFINIDO".equals(tag.toUpperCase())) {
			return this;
		}

		if (tags == null) {
			tags = new ArrayList<String>();
		}

		tags.add("\"" + tag.replaceAll("'", "").replaceAll(",", " ").replaceAll("  ", " ").trim() + "\"");

		return this;
	}

	public GeoDataModel getGeoData() {
		return geoData;
	}

	public void setGeoData(GeoDataModel geoData) {
		this.geoData = geoData;
	}

	public PhotoUploadModel withGeoData(GeoDataModel geoData) {
		this.geoData = geoData;
		return this;
	}

	@Override
	public String toString() {
		return "PhotoUploadModel [fileName=" + fileName + ", name=" + name + ", albumName=" + albumName
				+ ", description=" + description + ", tags=" + StringUtilities.join(tags, " ") + ", geoData=" + geoData
				+ "]";
	}

}
