package org.pelayo.util;

import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.net.URL;

public class QuotaGuardProxySelector extends ProxySelector {
	// Keep a reference on the previous default
	ProxySelector defsel = null;
	List<String> hostExceptions = new ArrayList<String>();

	/*
	 * Inner class representing a Proxy and a few extra data
	 */
	class InnerProxy {
		Proxy proxy;
		SocketAddress addr;
		// How many times did we fail to reach this proxy?
		int failedCount = 0;

		InnerProxy(InetSocketAddress a) {
			addr = a;
			proxy = new Proxy(Proxy.Type.SOCKS, a);
		}

		SocketAddress address() {
			return addr;
		}

		Proxy toProxy() {
			return proxy;
		}

		int failed() {
			return ++failedCount;
		}
	}

	/*
	 * A list of proxies, indexed by their address.
	 */
	HashMap<SocketAddress, InnerProxy> proxies = new HashMap<SocketAddress, InnerProxy>();

	public QuotaGuardProxySelector(ProxySelector def) {
		// Save the previous default
		defsel = def;
		String proxyUrlEnv = System.getenv("QUOTAGUARDSTATIC_URL");

		if (proxyUrlEnv != null) {
			try {
				URL proxyUrl = new URL(proxyUrlEnv);
				String proxyHost = proxyUrl.getHost();
				// Populate the HashMap (List of proxies)
				InnerProxy i = new InnerProxy(new InetSocketAddress(proxyHost, 1080));
				proxies.put(i.address(), i);
				System.out.println("Routing traffic through QuotaGuard Static host " + proxyHost);
				// Create list of IP/hostnames to include
				String qgMask = System.getenv("QUOTAGUARDSTATIC_MASK");
				if (qgMask != null) {
					System.out.println("QuotaGuard Static only routing for traffic to " + qgMask);
					String parts[] = qgMask.split(",");
					this.hostExceptions = Arrays.asList(parts);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("You need to set the environment variable QUOTAGUARDSTATIC_URL!");
		}

	}

	public boolean shouldProxy(String host) {
		for (int i = 0; i < hostExceptions.size(); i++) {
			if (host.equalsIgnoreCase(hostExceptions.get(i))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * This is the method that the handlers will call. Returns a List of proxy.
	 */
	public java.util.List<Proxy> select(URI uri) {
		// Let's stick to the specs.
		if (uri == null) {
			throw new IllegalArgumentException("URI can't be null.");
		}

		/*
		 * If it's a http (or https) URL, then we use our own list.
		 */
		String host = uri.getHost();
		if (shouldProxy(host)) {
			System.out.println("Got a match for " + host + " so lets proxy!");
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			for (InnerProxy p : proxies.values()) {
				l.add(p.toProxy());
			}
			return l;
		}
		System.out.println("Host " + host + " doesn't match a mask so no proxy");
		/*
		 * Not HTTP or HTTPS (could be SOCKS or FTP) defer to the default
		 * selector.
		 */
		if (defsel != null) {
			return defsel.select(uri);
		} else {
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			l.add(Proxy.NO_PROXY);
			return l;
		}
	}

	/*
	 * Method called by the handlers when it failed to connect to one of the
	 * proxies returned by select().
	 */
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		// Let's stick to the specs again.
		if (uri == null || sa == null || ioe == null) {
			throw new IllegalArgumentException("Arguments can't be null.");
		}

		/*
		 * Let's lookup for the proxy
		 */
		InnerProxy p = proxies.get(sa);
		if (p != null) {
			/*
			 * It's one of ours, if it failed more than 3 times let's remove it
			 * from the list.
			 */
			if (p.failed() >= 3)
				proxies.remove(sa);
		} else {
			/*
			 * Not one of ours, let's delegate to the default.
			 */
			if (defsel != null)
				defsel.connectFailed(uri, sa, ioe);
		}
	}
}
