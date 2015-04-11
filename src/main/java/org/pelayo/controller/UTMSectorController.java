package org.pelayo.controller;

import java.util.List;

import org.pelayo.dao.UTMSectorRepository;
import org.pelayo.model.UtmSector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UTMSectorController {

	@Autowired
	private UTMSectorRepository UTMSectorRepository;

	@RequestMapping("/utmSector")
	public List<UtmSector> utmSector() {
		return UTMSectorRepository.findConCita();
	}

}
