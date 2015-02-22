package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.FicherosFoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FicherosFotoRepository extends JpaRepository<FicherosFoto, Long> {

    List<FicherosFoto> findByFlickrId(String flickrId);

}
