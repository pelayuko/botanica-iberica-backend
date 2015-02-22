package org.pelayo.dev.flickr.commands.base;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.pelayo.dev.flickr.model.AuthorizedCommandModel;
import org.pelayo.dev.flickr.model.IModel;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;

public abstract class AbstractAuthorizedBaseCommand<T, Y extends IModel> extends AbstractBaseCommand {

	private AuthStore authStore;
	protected String userId;

	public AbstractAuthorizedBaseCommand(AuthorizedCommandModel model) throws Exception {
		super(model.getFlickr());

		String authsDirStr = System.getProperty("user.home") + File.separatorChar + ".flickrAuth";
		File authsDir = new File(authsDirStr);
		if (authsDir != null) {
			this.authStore = new FileAuthStore(authsDir);
		}

		String username = model.getUsername();
		if (StringUtils.isEmpty(username)) {
			throw new IllegalArgumentException("Username cannot be empty");
		}
		populateNsid(username);
		setupAuthInContext(null, username, null);
	}

	private void populateNsid(String username) throws FlickrException {

		Auth auth = null;
		if (authStore != null) {
			auth = authStore.retrieve(username); // assuming FileAuthStore
													// is enhanced else need
													// to
			// keep in user-level files.

			if (auth != null) {
				userId = auth.getUser().getId();
			}
		}
		if (auth != null)
			return;

		Auth[] allAuths = authStore.retrieveAll();
		for (int i = 0; i < allAuths.length; i++) {
			if (username.equals(allAuths[i].getUser().getUsername())) {
				userId = allAuths[i].getUser().getId();
				return;
			}
		}

		// For this to work: REST.java or PeopleInterface needs to change to
		// pass apiKey
		// as the parameter to the call which is not authenticated.

		// Get nsid using flickr.people.findByUsername
		PeopleInterface peopleInterf = flickr.getPeopleInterface();
		User u = peopleInterf.findByUsername(username);
		if (u != null) {
			userId = u.getId();
		}
	}

	private void authorize() throws IOException, SAXException, FlickrException {
		AuthInterface authInterface = flickr.getAuthInterface();
		Token accessToken = authInterface.getRequestToken();

		// Try with DELETE permission. At least need write permission for upload
		// and add-to-set.
		String url = authInterface.getAuthorizationUrl(accessToken, Permission.DELETE);
		System.out.println("Follow this URL to authorise yourself on Flickr");
		System.out.println(url);
		System.out.println("Paste in the token it gives you:");
		System.out.print(">>");

		Scanner scanner = new Scanner(System.in);
		String tokenKey = scanner.nextLine();

		Token requestToken = authInterface.getAccessToken(accessToken, new Verifier(tokenKey));

		Auth auth = authInterface.checkToken(requestToken);
		RequestContext.getRequestContext().setAuth(auth);
		this.authStore.store(auth);
		scanner.close();
		System.out.println("Thanks.  You probably will not have to do this every time. Auth saved for user: "
				+ auth.getUser().getUsername() + " nsid is: " + auth.getUser().getId());
		System.out.println(" AuthToken: " + auth.getToken() + " tokenSecret: " + auth.getTokenSecret());
	}

	/**
	 * If the Authtoken was already created in a separate program but not saved
	 * to file.
	 * 
	 * @param authToken
	 * @param tokenSecret
	 * @param username
	 * @return
	 * @throws IOException
	 */
	private Auth constructAuth(String authToken, String tokenSecret, String username) throws IOException {

		Auth auth = new Auth();
		auth.setToken(authToken);
		auth.setTokenSecret(tokenSecret);

		// Prompt to ask what permission is needed: read, update or delete.
		auth.setPermission(Permission.fromString("delete"));

		User user = new User();
		// Later change the following 3. Either ask user to pass on command line
		// or read
		// from saved file.
		user.setId(userId);
		user.setUsername((username));
		user.setRealName("");
		auth.setUser(user);
		this.authStore.store(auth);
		return auth;
	}

	/**
	 * @param authToken
	 * @param username
	 * @param tokenSecret
	 * @throws IOException
	 * @throws SAXException
	 * @throws FlickrException
	 */
	private void setupAuthInContext(String authToken, String username, String tokenSecret) throws Exception {
		RequestContext rc = RequestContext.getRequestContext();
		Auth auth = null;

		if (authToken != null && !authToken.equals("") && tokenSecret != null && !tokenSecret.equals("")) {
			auth = constructAuth(authToken, tokenSecret, username);
			rc.setAuth(auth);
		} else {
			setupAuthInContext();
		}
	}

	protected void setupAuthInContext() throws Exception {
		RequestContext rc = RequestContext.getRequestContext();

		if (this.authStore != null) {
			Auth auth = this.authStore.retrieve(this.userId);
			if (auth == null) {
				this.authorize();
			} else {
				rc.setAuth(auth);
			}
		}
	}

	public abstract T execute(Y model) throws Exception;

}
