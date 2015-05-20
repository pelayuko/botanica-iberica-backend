package org.pelayo;

import java.net.Authenticator;
import java.net.ProxySelector;

import org.pelayo.util.QuotaGuardProxyAuthenticator;
import org.pelayo.util.QuotaGuardProxySelector;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	/*
	 * This method is called during Spring's startup.
	 * 
	 * @param event Event raised when an ApplicationContext gets initialized or
	 * refreshed.
	 */
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		System.out.println("Initializing SOCKS Proxy");
//		// SOCKS Proxy
//		QuotaGuardProxyAuthenticator proxy = new QuotaGuardProxyAuthenticator();
//		Authenticator.setDefault(proxy.getAuth());
//		QuotaGuardProxySelector ps = new QuotaGuardProxySelector(ProxySelector.getDefault());
//		ProxySelector.setDefault(ps);

		return;
	}

} // class ApplicationStartup
