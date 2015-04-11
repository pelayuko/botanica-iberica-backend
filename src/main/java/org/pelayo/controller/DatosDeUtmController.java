package org.pelayo.controller;

import java.util.List;

import org.pelayo.controller.model.TaxonResponse;
import org.pelayo.dao.TaxonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeUtmController {
	
	@Autowired
	private TaxonesRepository repo;

    @RequestMapping("/datosDeUtm")
    public List<TaxonResponse> datosDeUtm(@RequestParam(value = "utm", required = true) String utm) {
        return repo.taxonesByUTM(utm);
    }
        
}
