package org.pelayo.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

	@RequestMapping("/echo")
	public String echo() throws UnknownHostException, SocketException {
		StringBuilder sb = new StringBuilder();

		sb.append("Your Host addr: " + InetAddress.getLocalHost().getHostAddress() + "\n");
		Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		for (; n.hasMoreElements();) {
			NetworkInterface e = n.nextElement();

			Enumeration<InetAddress> a = e.getInetAddresses();
			for (; a.hasMoreElements();) {
				InetAddress addr = a.nextElement();
				sb.append("  " + addr.getHostAddress() + "\n");
			}
		}

		return sb.toString();
	}
}
