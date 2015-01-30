package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CitasRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByLugar(String lugar);
    
    @Query("select l from Cita l where l.lugare.nombre like '%:lugar%'")
    List<Cita> findByLugar2(@Param("lugar") String lugar);
}
