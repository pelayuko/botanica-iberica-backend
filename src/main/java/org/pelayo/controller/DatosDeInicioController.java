package org.pelayo.controller;

import java.util.ArrayList;
import java.util.List;

import org.pelayo.controller.model.FotoResponse;
import org.pelayo.dao.CitasRepository;
import org.pelayo.dao.DatosDeEspecieRepository;
import org.pelayo.dao.DatosDeZonaRepository;
import org.pelayo.dao.EspecieRepository;
import org.pelayo.dao.FotosLugaresRepository;
import org.pelayo.dao.FotosPlantasRepository;
import org.pelayo.dao.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatosDeInicioController {
	
	@Autowired
	private DatosDeEspecieRepository especieRepo;
	
	@Autowired
	private DatosDeZonaRepository zonaRepo;
	
	@Autowired
	private CitasRepository citasRepository;
	
	@Autowired
	private ZonaRepository zonaRepository;
	
	@Autowired
	private EspecieRepository especieRepository;
	
	@Autowired
	private FotosPlantasRepository fotosPlantasRepository;
	
	@Autowired
	private FotosLugaresRepository fotosLugaresRepository;


    @RequestMapping("/datosDeInicioEsp")
    public List<FotoResponse> datosDeInicioEsp(@RequestParam(value = "count", defaultValue = "4") Integer count) {
    	List<FotoResponse> fotos = new ArrayList<FotoResponse>();
    	
    	fotos.addAll(especieRepo.fotosDeEspecieAlAzar(count));

        return fotos;
    }
    
    @RequestMapping("/datosDeInicioZon")
    public List<FotoResponse> datosDeInicioZon(@RequestParam(value = "count", defaultValue = "4") Integer count) {
    	List<FotoResponse> fotos = new ArrayList<FotoResponse>();
    	
    	fotos.addAll(zonaRepo.fotosDeZonasAlAzar(count));

        return fotos;
    }
    
    @RequestMapping("/datosDeInicioTema")
    public List<FotoResponse> datosDeInicioTema(@RequestParam(value = "tema", defaultValue = "") String tema) {
    	List<FotoResponse> fotos = new ArrayList<FotoResponse>();
    	if (tema.length() == 0) fotos.addAll(zonaRepo.fotosDeZonasAlAzar(4));
    	else fotos.addAll(zonaRepo.fotosDeZonasPorTema(tema, 16));

        return fotos;
    }
    
    @RequestMapping("/fotosPortada")
    public List<FotoResponse> fotosPortada(@RequestParam(value = "count", defaultValue = "4") Integer count) {
    	List<FotoResponse> fotos = new ArrayList<FotoResponse>();
    	
    	fotos.addAll(zonaRepo.fotosDeZonasAlAzar(count/2));
    	fotos.addAll(especieRepo.fotosDeEspecieAlAzar(count/2));

        return fotos;
    }
    
    @RequestMapping("/numeroEspecies")
    public Long numeroEspecies() {
        return especieRepository.count();
    }
    
    @RequestMapping("/numeroCitas")
    public Long numeroCitas() {
        return citasRepository.count();
    }
    
    @RequestMapping("/numeroZonas")
    public Long numeroZonas() {
        return zonaRepository.count();
    }
    
    @RequestMapping("/numeroFotosPlantas")
    public Long numeroFotosPlantas() {
        return fotosPlantasRepository.count();
    }
    
    
    @RequestMapping("/numeroFotosLugares")
    public Long numeroFotosLugares() {
        return fotosLugaresRepository.count();
    }
}
