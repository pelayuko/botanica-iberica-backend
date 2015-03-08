package org.pelayo.dao;

import org.pelayo.model.FotoPlanta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotosPlantasRepository extends FotosRepository<FotoPlanta>, JpaRepository<FotoPlanta, Integer> {

}
