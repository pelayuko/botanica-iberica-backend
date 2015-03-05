package org.pelayo.dev.flickr.commands.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.PhotoUploadModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.springframework.util.StringUtils;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.prefs.PrefsInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;

/**
 * A simple program to upload photos to a set. It checks for files already
 * uploaded assuming the title is not changed so that it can be rerun if partial
 * upload is done. It uses the tag field to store the filename as OrigFileName
 * to be used while downloading if the title has been changed. If
 * setup.properties is not available, pass the apiKey and secret as arguments to
 * the program.
 * 
 * This sample also uses the AuthStore interface, so users will only be asked to
 * authorize on the first run.
 * 
 * Please NOTE that this needs Java 7 to work. Java 7 was released on July 28,
 * 2011 and soon Java 6 may not be supported anymore ( Jul 2014).
 * 
 * @author Keyur Parikh
 */

public class PhotoUploader extends AbstractAuthorizedBaseCommand<Photo, PhotoUploadModel> {

	private static final Logger logger = Logger.getLogger(PhotoUploader.class);

	public boolean flickrDebug = false;

	private int privacy = -1;

	private HashMap<String, String> allSetsMap = new HashMap<String, String>();

	public static final SimpleDateFormat smp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss a");

	public PhotoUploader(AuthorizedCommandModel model) throws Exception {
		super(model);

		if (!canUpload()) {
			throw new RuntimeException("Cannot upload!");
		}

		fetchPhotosetsInfo();
	}

	private int getPrivacy() throws Exception {

		PrefsInterface prefi = flickr.getPrefsInterface();
		privacy = prefi.getPrivacy();

		return (privacy);
	}

	public String uploadfile(PhotoUploadModel model) throws Exception {
		String photoId;
		String filename = model.getFileName();

		setupAuthInContext();

		// PhotosetsInterface pi = flickr.getPhotosetsInterface();
		// PhotosInterface photoInt = flickr.getPhotosInterface();
		// Map<String, Collection> allPhotos = new HashMap<String,
		// Collection>();
		/**
		 * 1 : Public 2 : Friends only 3 : Family only 4 : Friends and Family 5
		 * : Private
		 **/
		// if (privacy == -1)
		// getPrivacy();
		// PRIVATE
		// privacy = 5;
		// PUBLIC
		privacy = 1;

		UploadMetaData metaData = new UploadMetaData();

		metaData.setDescription(model.getDescription());

		if (privacy == 1)
			metaData.setPublicFlag(true);
		if (privacy == 2 || privacy == 4)
			metaData.setFriendFlag(true);
		if (privacy == 3 || privacy == 4)
			metaData.setFamilyFlag(true);

		if (basefilename == null || basefilename.equals(""))
			basefilename = filename; // "image.jpg";

		String title = basefilename;
		boolean setMimeType = true; // change during testing. Doesn't seem to be
									// supported at this time in flickr.
		if (setMimeType) {
			if (basefilename.lastIndexOf('.') > 0) {
				title = basefilename.substring(0, basefilename.lastIndexOf('.'));
				String suffix = basefilename.substring(basefilename.lastIndexOf('.') + 1);
				// Set Mime Type if known.

				// Later use a mime-type properties file or a hash table of all
				// known photo and video types
				// allowed by flickr.

				if (suffix.equalsIgnoreCase("png")) {
					metaData.setFilemimetype("image/png");
				} else if (suffix.equalsIgnoreCase("mpg") || suffix.equalsIgnoreCase("mpeg")) {
					metaData.setFilemimetype("video/mpeg");
				} else if (suffix.equalsIgnoreCase("mov")) {
					metaData.setFilemimetype("video/quicktime");
				}
			}
		}
		logger.debug(" File : " + filename);
		logger.debug(" basefilename : " + basefilename);

		if (!StringUtils.isEmpty(model.getName())) {
			title = model.getName();
			logger.debug(" title : " + model.getName());
			metaData.setTitle(model.getName());
		} // flickr defaults the title field from file name.

		// UploadMeta is using String not Tag class.

		// Tags are getting mangled by yahoo stripping off the = , '.' and many
		// other punctuation characters
		// and converting to lower case: use the raw tag field to find the real
		// value for checking and
		// for download.
		List<String> tags = new ArrayList<String>();

		// for (String t : model.getTags()) {
		// tags.add(t.replaceAll(" ", ""));
		// }
		tags.addAll(model.getTags());

		String tmp = basefilename;
		basefilename = FlickrHelper.normalize(basefilename, false);
		tags.add("OrigFileName='" + basefilename + "'");
		metaData.setTags(tags);

		if (!tmp.equals(basefilename)) {
			System.out.println(" File : " + basefilename + " contains special characters.  stored as " + basefilename
					+ " in tag field");
		}

		// File imageFile = new File(filename);
		// InputStream in = null;
		Uploader uploader = flickr.getUploader();

		try {
			metaData.setFilename(basefilename);
			// check correct handling of escaped value

			File f = new File(filename);
			photoId = uploader.upload(f, metaData);

			logger.debug(" File : " + filename + " uploaded: photoId = " + photoId);
		} finally {

		}

		return (photoId);
	}

	private void fetchPhotosetsInfo() {

		PhotosetsInterface pi = flickr.getPhotosetsInterface();
		try {
			int page = 1;
			while (true) {
				Photosets photosets = pi.getList(userId, 500, page, null);
				Collection<Photoset> setsColl = photosets.getPhotosets();
				Iterator<Photoset> setsIter = setsColl.iterator();
				while (setsIter.hasNext()) {
					Photoset set = setsIter.next();
					allSetsMap.put(set.getTitle(), set.getId());
				}

				if (setsColl.size() < 500) {
					break;
				}
				page++;
			}
			logger.info(" Sets retrieved: " + allSetsMap.size());
		} catch (FlickrException e) {
			e.printStackTrace();
		}
	}

	private String basefilename = null;

	@Override
	public Photo execute(PhotoUploadModel model) throws Exception {
		String photoid;

		String filename = model.getFileName();
		if (filename.equals("")) {
			System.out.println("filename must be entered for uploadfile ");
			return null;
		}
		if (filename.lastIndexOf(File.separatorChar) > 0)
			basefilename = filename.substring(filename.lastIndexOf(File.separatorChar) + 1, filename.length());
		else
			basefilename = filename;

		if (!FlickrHelper.isValidSuffix(basefilename)) {
			System.out.println(" File: " + basefilename + " is not a supported filetype for flickr (invalid suffix)");
			return null;
		}

		File f = new File(filename);
		if (!f.exists() || !f.canRead()) {
			System.out.println(" File: " + filename + " cannot be processed, does not exist or is unreadable.");
			return null;
		}
		logger.debug("Calling uploadfile for filename : " + filename);
		long initTime = System.currentTimeMillis();

		// logger.info("Upload of " + filename + " started at: " +
		// smp.format(new Date()) + "\n");

		photoid = uploadfile(model);
		// Add to Set. Create set if it does not exist.
		if (photoid != null) {
			addPhotoToSet(photoid, model.getAlbumName());
		}
		// logger.info("Upload of " + filename + " finished at: " +
		// smp.format(new Date()) + "\n");
		logger.info("Upload took " + (System.currentTimeMillis() - initTime) + "millis\n");

		if (model.getGeoData() != null) {
			GeoData geoData = new GeoData();
			{
				geoData.setLatitude(model.getGeoData().getLat());
				geoData.setLongitude(model.getGeoData().getLng());
				geoData.setAccuracy(Flickr.ACCURACY_STREET);
			}

			flickr.getGeoInterface().setLocation(photoid, geoData);
		}

		return flickr.getPhotosInterface().getPhoto(photoid);
	}

	private boolean canUpload() {
		RequestContext rc = RequestContext.getRequestContext();
		Auth auth = null;
		auth = rc.getAuth();
		if (auth == null) {
			System.out.println(" Cannot upload, there is no authorization information.");
			return false;
		}
		Permission perm = auth.getPermission();
		if ((perm.getType() == Permission.WRITE_TYPE) || (perm.getType() == Permission.DELETE_TYPE))
			return true;
		else {
			System.out.println(" Cannot upload, You need write or delete permission, you have : " + perm.toString());
			return false;
		}
	}

	private void addPhotoToSet(String photoid, String setName) throws Exception {
		PhotosetsInterface psetsInterface = flickr.getPhotosetsInterface();

		String photoSetId = allSetsMap.get(setName);
		if (photoSetId == null) {
			photoSetId = psetsInterface.create(setName, "", photoid).getId();
			allSetsMap.put(setName, photoSetId);
		}
		psetsInterface.addPhoto(photoSetId, photoid);
	}
}