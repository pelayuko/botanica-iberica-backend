package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.Localidentif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocalidentifRepository extends JpaRepository<Localidentif, Long> {

    List<Localidentif> findByLugar(String lugar);
    
    @Query("select l from Localidentif l where l.lugare.nombre like '%:lugar%'")
    List<Localidentif> findByLugar2(@Param("lugar") String lugar);
}
