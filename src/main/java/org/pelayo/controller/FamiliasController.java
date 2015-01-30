package org.pelayo.controller;

import java.util.List;

import org.pelayo.dao.FamiliasRepository;
import org.pelayo.model.Familia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamiliasController {

	@Autowired
	private FamiliasRepository familiasRepository;

	@RequestMapping("/familias")
	public List<Familia> citas() {
		return familiasRepository.findAll();
	}

}
