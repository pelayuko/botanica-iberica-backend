package org.pelayo.controller;

import java.util.ArrayList;
import java.util.List;

import org.pelayo.controller.model.SearchResponse;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

	@Autowired
	private TaxonesRepository repo;

	@RequestMapping("/searchAll")
	public List<SearchResponse> searchAll(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "10") String limit) {
		List<SearchResponse> response = new ArrayList<SearchResponse>();

		response.addAll(repo.buscaNombreZonas(nombre));
		response.addAll(repo.buscaNombreGenero(nombre, Integer.parseInt(limit)));
		response.addAll(repo.buscaNombreFamilia(nombre, Integer.parseInt(limit)));
		response.addAll(repo.buscaNombreComun(nombre, Integer.parseInt(limit)));
		response.addAll(repo.buscaNombreEspecie(nombre, Integer.parseInt(limit), false));

		return response;
	}

	@RequestMapping("/searchTodos")
	public List<SearchResponse> buscaSinonimos(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "100") String limit) {
		return repo.buscaSinonimos(nombre, Integer.parseInt(limit));
	}

	@RequestMapping("/searchComunes")
	public List<SearchResponse> buscaNombreComun(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "100") String limit) {
		return repo.buscaNombreComun(nombre, Integer.parseInt(limit));
	}

	@RequestMapping("/searchAccepted")
	public List<SearchResponse> buscaNombreEspecie(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "100") String limit) {
		return repo.buscaNombreEspecie(nombre, Integer.parseInt(limit), true);
	}

	@RequestMapping("/searchAcceptedStarting")
	public List<SearchResponse> buscaNombreEspecieComienzo(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "100") String limit) {
		return repo.buscaNombreEspecie(nombre, Integer.parseInt(limit), true);
	}

	@RequestMapping("/searchGenus")
	public List<SearchResponse> buscaNombreGenero(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "100") String limit) {
		return repo.buscaNombreGenero(nombre, Integer.parseInt(limit));
	}
	
	@RequestMapping("/searchFamily")
	public List<SearchResponse> buscaNombreFamilia(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "100") String limit) {
		return repo.buscaNombreFamilia(nombre, Integer.parseInt(limit));
	}
	
	@RequestMapping("/searchZone")
	public List<SearchResponse> buscaNombreZonas(@RequestParam(value = "nombre", required = true) String nombre,
			@RequestParam(value = "limit", defaultValue = "100") String limit) {
		return repo.buscaNombreZonas(nombre);
	}

}
