package org.pelayo.dao;

import org.pelayo.model.Especie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieRepository extends JpaRepository<Especie, Integer> {

}
