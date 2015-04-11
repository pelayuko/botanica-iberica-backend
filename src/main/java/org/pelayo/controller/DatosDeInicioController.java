package org.pelayo.controller;

import java.util.ArrayList;
import java.util.List;

import org.pelayo.controller.model.FotoResponse;
import org.pelayo.dao.DatosDeEspecieRepository;
import org.pelayo.dao.DatosDeZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeInicioController {
	
	@Autowired
	private DatosDeEspecieRepository especieRepo;
	
	@Autowired
	private DatosDeZonaRepository zonaRepo;

    @RequestMapping("/datosDeInicioEsp")
    public List<FotoResponse> datosDeInicioEsp(Model model) {
    	List<FotoResponse> fotos = new ArrayList<FotoResponse>();
    	
    	fotos.addAll(especieRepo.fotosDeEspecieAlAzar(4));
//    	fotos.addAll(zonaRepo.fotosDeZonaAlAzar(5));

        return fotos;
    }
    
    @RequestMapping("/datosDeInicioZon")
    public List<FotoResponse> datosDeInicioZon(Model model) {
    	List<FotoResponse> fotos = new ArrayList<FotoResponse>();
    	
//    	fotos.addAll(especieRepo.fotosDeEspecieAlAzar(5));
    	fotos.addAll(zonaRepo.fotosDeZonaAlAzar(4));

        return fotos;
    }
    

}
