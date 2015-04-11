package org.pelayo.controller.model;

import java.util.HashMap;
import java.util.Map;

public class InfoTaxonResponse {

	static final Map<String, String> frecuencias;

	static {
		frecuencias = new HashMap<String, String>();
		String[][] pairs = { { "RR", "Muy rara" }, { "R", "Rara" }, { "C", "Común" }, { "CC", "Muy común" },
				{ "M", "Normal" }, { "NA", "NA" } };
		for (String[] pair : pairs) {
			frecuencias.put(pair[0], pair[1]);
		}
	}

	public String color;
	public String fitoTipo;
	public String fitoSubtipo;
	public String presComarca;
	public String frecComarca;
	public String mesiniflor;
	public String mesfinflor;
	public String comentario;

	public InfoTaxonResponse(String color, String fitotipo, String fitosubtipo, String presComarca, String freccomarca,
			String comentario) {
		this.color = color;
		this.fitoTipo = fitotipo;
		this.fitoSubtipo = fitosubtipo;
		this.presComarca = presComarca;
		this.frecComarca = frecuencias.get(freccomarca);
		this.comentario = comentario;
	}
}
