package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.UtmSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UTMSectorRepository extends JpaRepository<UtmSector, String> {

    @Query("select t from UtmSector t where t.conCita = 1")
    List<UtmSector> findConCita();
}
