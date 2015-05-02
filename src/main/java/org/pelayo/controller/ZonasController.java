package org.pelayo.controller;

import java.util.List;

import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZonasController {

	// FIXME: do it with query -- Arreglado
/*	static public String[][] SECTORES = { { "A", "La Muela" }, { "B", "Campo de Borja" }, { "C", "La Sierra" },
			{ "D", "El Moncayo" }, { "E", "Somontano" }, { "F", "Magallón" }, { "G", "Tarazona" }, { "H", "Ágreda" },
			{ "Z", "Otras" } };

	public static String nombreSectorFromEtiq(String etiq){
		int temp = (int) (etiq.charAt(0)) - 64;
		if (temp < 10)
			return SECTORES[temp - 1][1];
		else return "Desconocida";
	}*/
	
	@Autowired
	private TaxonesRepository repo;

	@RequestMapping("/zonasBySector")
	public List<String> zonasBySector(@RequestParam(value = "sector", required = true) String sector) {
		return repo.getNombreZonasBySector(sector);
	}

}
