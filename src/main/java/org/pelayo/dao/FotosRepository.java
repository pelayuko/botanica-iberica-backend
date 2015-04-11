package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.IFoto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FotosRepository<T extends IFoto> {

	List<T> findAll();

	@Query("select fl.ficherosfoto.flickrUrl from #{#entityName} fl order by RAND()")
	List<String> findRandomUrl(Pageable pageable);

	@Query("select fl from #{#entityName} fl where fl.ficherosfoto.flickrStatus is null or fl.ficherosfoto.flickrStatus != 'SUCCESS'")
	List<T> findNotUploadedToFickr();

	@Query("select fl from #{#entityName} fl where fl.ficherosfoto.flickrId = :id")
	T findByFlickrId(@Param("id") String flickrId);

	T findByIdIdent(Integer idIdent);
}