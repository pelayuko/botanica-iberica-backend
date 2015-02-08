package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FamiliasRepository extends JpaRepository<Familia, Long> {

	@Query("SELECT f.nombreFam FROM Familia f")
	List<String> findAllNames();
}
