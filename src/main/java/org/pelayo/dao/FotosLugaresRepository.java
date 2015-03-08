package org.pelayo.dao;

import org.pelayo.model.FotoLugar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotosLugaresRepository extends FotosRepository<FotoLugar>, JpaRepository<FotoLugar, Integer> {

}
