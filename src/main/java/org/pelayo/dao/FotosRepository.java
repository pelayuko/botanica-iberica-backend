package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.IFoto;

public interface FotosRepository<T extends IFoto> {

	List<T> findNotUploadedToFickr();

	T findByFlickrId(String flickrId);

	List<T> findAll();
}