package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.FotoLugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FotosLugaresRepository extends JpaRepository<FotoLugar, Integer>, FotosRepository<FotoLugar> {

	@Query("select fl from FotoLugar fl where fl.ficherosfoto.flickrStatus is null or fl.ficherosfoto.flickrStatus != 'SUCCESS'")
	List<FotoLugar> findNotUploadedToFickr();

	@Query("select fl from FotoLugar fl where fl.ficherosfoto.flickrId = :id")
	FotoLugar findByFlickrId(@Param("id") String flickrId);

	FotoLugar findByIdIdent(Integer idIdent);
}
