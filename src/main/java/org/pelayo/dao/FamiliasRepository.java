package org.pelayo.dao;

import org.pelayo.model.Familia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamiliasRepository extends JpaRepository<Familia, Long> {
}
