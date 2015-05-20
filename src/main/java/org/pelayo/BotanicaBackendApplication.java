package org.pelayo;

import java.net.Authenticator;
import java.net.ProxySelector;

import org.pelayo.util.QuotaGuardProxyAuthenticator;
import org.pelayo.util.QuotaGuardProxySelector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class BotanicaBackendApplication {

	public static void main(String[] args) {
		System.out.println("Initializing SOCKS Proxy");
		// SOCKS Proxy
		QuotaGuardProxyAuthenticator proxy = new QuotaGuardProxyAuthenticator();
		Authenticator.setDefault(proxy.getAuth());
		QuotaGuardProxySelector ps = new QuotaGuardProxySelector(ProxySelector.getDefault());
		ProxySelector.setDefault(ps);
		
		
		SpringApplication.run(BotanicaBackendApplication.class, args);
	}
}
