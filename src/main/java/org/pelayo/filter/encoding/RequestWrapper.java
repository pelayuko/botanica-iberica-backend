package org.pelayo.filter.encoding;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String[] getParameterValues(String parameter) {
		String[] results = super.getParameterValues(parameter);

		if (results == null) {
			return null;
		}

		int count = results.length;

		String[] trimResults = new String[count];

		for (int i = 0; i < count; i++) {
			trimResults[i] = results[i].replaceAll("Ã¡", "á").replaceAll("Ã©", "é").replaceAll("Ã­", "í")
					.replaceAll("Ã³", "ó").replaceAll("Ãº", "ú").replaceAll("Ã±", "ñ").replaceAll("Ã§", "ç")
					.replaceAll("Ã£", "ã").replaceAll("Âº", "º").replaceAll("Ã¼","ü");
		}

		return trimResults;
	}

}
