package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.FotoLugar;
import org.pelayo.model.FotoPlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FotosPlantasRepository extends JpaRepository<FotoPlanta, Long>, FotosRepository<FotoPlanta> {

	@Query("select fl from FotoPlanta fl where fl.ficherosfoto.flickrStatus is null or fl.ficherosfoto.flickrStatus != 'SUCCESS'")
	List<FotoPlanta> findNotUploadedToFickr();

	@Query("select fl from FotoPlanta fl where fl.ficherosfoto.flickrId = :id")
	FotoPlanta findByFlickrId(@Param("id") String flickrId);

}
