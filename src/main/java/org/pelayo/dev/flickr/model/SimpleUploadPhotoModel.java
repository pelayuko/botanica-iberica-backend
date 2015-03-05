package org.pelayo.dev.flickr.model;

public class SimpleUploadPhotoModel implements IModel {

	public enum Mode {
		FLOWER, LANDSCAPE
	}

	private Mode mode;

	private String photoId;

	public static SimpleUploadPhotoModel mk() {
		return new SimpleUploadPhotoModel();
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public SimpleUploadPhotoModel withMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public SimpleUploadPhotoModel withPhotoId(String photoId) {
		this.photoId = photoId;
		return this;
	}
}
