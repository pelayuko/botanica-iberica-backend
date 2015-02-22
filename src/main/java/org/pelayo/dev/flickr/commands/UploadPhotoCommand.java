package org.pelayo.dev.flickr.commands;

// import java.io.BufferedInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
// import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
// import java.io.ByteArrayOutputStream;
//import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.pelayo.dev.flickr.commands.base.AbstractAuthorizedBaseCommand;
import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.PhotoUploadModel;
import org.pelayo.dev.flickr.util.FlickrHelper;
import org.springframework.util.StringUtils;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
// import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
// import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
// import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.prefs.PrefsInterface;
// import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
// import java.io.FileOutputStream;
// import java.util.Map;

// import com.flickr4java.flickr.tags.Tag;

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

public class UploadPhotoCommand extends AbstractAuthorizedBaseCommand<Photo, PhotoUploadModel> {

	private static final Logger logger = Logger.getLogger(UploadPhotoCommand.class);

	public boolean flickrDebug = false;

	private int privacy = -1;

	HashMap<String, Photoset> allSetsMap = new HashMap<String, Photoset>();

	HashMap<String, ArrayList<String>> setNameToId = new HashMap<String, ArrayList<String>>();

	public static final SimpleDateFormat smp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss a");

	public UploadPhotoCommand(AuthorizedCommandModel model) throws Exception {
		super(model);

		if (!canUpload()) {
			throw new RuntimeException("Cannot upload!");
		}
	}

	public int getPrivacy() throws Exception {

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

		// ByteArrayOutputStream out = null;
		try {
			// in = new FileInputStream(imageFile);
			// out = new ByteArrayOutputStream();

			// int b = -1;
			/**
			 * while ((b = in.read()) != -1) { out.write((byte) b); }
			 **/

			/**
			 * byte[] buf = new byte[1024]; while ((b = in.read(buf)) != -1) {
			 * // fos.write(read); out.write(buf, 0, b); }
			 **/

			metaData.setFilename(basefilename);
			// check correct handling of escaped value

			File f = new File(filename);
			photoId = uploader.upload(f, metaData);

			logger.debug(" File : " + filename + " uploaded: photoId = " + photoId);
		} finally {

		}

		return (photoId);
	}

	// FIXME: review this method
	public void getPhotosetsInfo() {

		PhotosetsInterface pi = flickr.getPhotosetsInterface();
		try {
			int setsPage = 1;
			while (true) {
				Photosets photosets = pi.getList(userId, 500, setsPage, null);
				Collection<Photoset> setsColl = photosets.getPhotosets();
				Iterator<Photoset> setsIter = setsColl.iterator();
				while (setsIter.hasNext()) {
					Photoset set = setsIter.next();
					allSetsMap.put(set.getId(), set);

					// 2 or more sets can in theory have the same name. !!!
					ArrayList<String> setIdarr = setNameToId.get(set.getTitle());
					if (setIdarr == null) {
						setIdarr = new ArrayList<String>();
						setIdarr.add(new String(set.getId()));
						setNameToId.put(set.getTitle(), setIdarr);
					} else {
						setIdarr.add(new String(set.getId()));
					}
				}

				if (setsColl.size() < 500) {
					break;
				}
				setsPage++;
			}
			logger.debug(" Sets retrieved: " + allSetsMap.size());
			// all_sets_retrieved = true;
			// Print dups if any.

			Set<String> keys = setNameToId.keySet();
			Iterator<String> iter = keys.iterator();
			while (iter.hasNext()) {
				String name = iter.next();
				ArrayList<String> setIdarr = setNameToId.get(name);
				if (setIdarr != null && setIdarr.size() > 1) {
					System.out.println("There is more than 1 set with this name : " + setNameToId.get(name));
					for (int j = 0; j < setIdarr.size(); j++) {
						System.out.println("           id: " + setIdarr.get(j));
					}
				}
			}

		} catch (FlickrException e) {
			e.printStackTrace();
		}
	}

	private String setid = null;

	private String basefilename = null;

	private final PhotoList<Photo> photos = new PhotoList<Photo>();

	private final HashMap<String, Photo> filePhotos = new HashMap<String, Photo>();

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

		boolean fileUploaded = checkIfLoaded(filename);

		if (!fileUploaded) {
			if (!FlickrHelper.isValidSuffix(basefilename)) {
				System.out.println(" File: " + basefilename
						+ " is not a supported filetype for flickr (invalid suffix)");
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
		} else {
			logger.info(" File: " + filename + " has already been loaded on " + getUploadedTime(filename));
			return null;
		}
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

	/**
	 * The assumption here is that for a given set only unique file-names will
	 * be loaded and the title field can be used. Later change to use the tags
	 * field ( OrigFileName) and strip off the suffix.
	 * 
	 * @param filename
	 * @return
	 */
	private boolean checkIfLoaded(String filename) {

		String title;
		if (basefilename.lastIndexOf('.') > 0)
			title = basefilename.substring(0, basefilename.lastIndexOf('.'));
		else
			return false;

		if (filePhotos.containsKey(title))
			return true;

		return false;
	}

	private String getUploadedTime(String filename) {

		String title = "";
		if (basefilename.lastIndexOf('.') > 0)
			title = basefilename.substring(0, basefilename.lastIndexOf('.'));

		if (filePhotos.containsKey(title)) {
			Photo p = filePhotos.get(title);
			if (p.getDatePosted() != null) {
				return (smp.format(p.getDatePosted()));
			}
		}

		return "";
	}

	public void getSetPhotos(String setName) throws FlickrException {
		// Check if this is an existing set. If it is get all the photo list to
		// avoid reloading already
		// loaded photos.
		ArrayList<String> setIdarr;
		setIdarr = setNameToId.get(setName);
		if (setIdarr != null) {
			setid = setIdarr.get(0);
			PhotosetsInterface pi = flickr.getPhotosetsInterface();

			Set<String> extras = new HashSet<String>();
			/**
			 * A comma-delimited list of extra information to fetch for each
			 * returned record. Currently supported fields are: license,
			 * date_upload, date_taken, owner_name, icon_server,
			 * original_format, last_update, geo, tags, machine_tags, o_dims,
			 * views, media, path_alias, url_sq, url_t, url_s, url_m, url_o
			 */

			extras.add("date_upload");
			extras.add("original_format");
			extras.add("media");
			// extras.add("url_o");
			extras.add("tags");

			int setPage = 1;
			while (true) {
				PhotoList<Photo> tmpSet = pi.getPhotos(setid, extras, Flickr.PRIVACY_LEVEL_NO_FILTER, 500, setPage);

				int tmpSetSize = tmpSet.size();
				photos.addAll(tmpSet);
				if (tmpSetSize < 500) {
					break;
				}
				setPage++;
			}
			for (int i = 0; i < photos.size(); i++) {
				filePhotos.put(photos.get(i).getTitle(), photos.get(i));
			}
			if (flickrDebug) {
				logger.debug("Set title: " + setName + "  id:  " + setid + " found");
				logger.debug("   Photos in Set already loaded: " + photos.size());
			}
		}
	}

	private void addPhotoToSet(String photoid, String setName) throws Exception {

		ArrayList<String> setIdarr;

		// all_set_maps.

		PhotosetsInterface psetsInterface = flickr.getPhotosetsInterface();

		Photoset set = null;

		if (setid == null) {
			// In case it is a new photo-set.
			setIdarr = setNameToId.get(setName);
			if (setIdarr == null) {
				// setIdarr should be null since we checked it getSetPhotos.
				// Create the new set.
				// set the setid .

				String description = "";
				set = psetsInterface.create(setName, description, photoid);
				setid = set.getId();

				setIdarr = new ArrayList<String>();
				setIdarr.add(new String(setid));
				setNameToId.put(setName, setIdarr);

				allSetsMap.put(set.getId(), set);
			}
		} else {
			set = allSetsMap.get(setid);
			psetsInterface.addPhoto(setid, photoid);
		}
		// Add to photos .

		// Add Photo to existing set.
		PhotosInterface photoInt = flickr.getPhotosInterface();
		Photo p = photoInt.getPhoto(photoid);
		if (p != null) {
			photos.add(p);
			String title;
			if (basefilename.lastIndexOf('.') > 0)
				title = basefilename.substring(0, basefilename.lastIndexOf('.'));
			else
				title = p.getTitle();
			filePhotos.put(title, p);
		}
	}
}