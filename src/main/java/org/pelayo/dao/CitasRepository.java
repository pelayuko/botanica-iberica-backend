package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CitasRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByLugar(String lugar);
    
    @Query("select c from Cita c where c.zona.nombre like '%:zona%'")
    List<Cita> findByNombreZona(@Param("zona") String zona);
}
