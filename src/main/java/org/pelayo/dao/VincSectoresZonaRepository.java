package org.pelayo.dao;

import java.util.List;

import org.pelayo.model.VincSectoresZona;
import org.pelayo.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VincSectoresZonaRepository extends
		JpaRepository<VincSectoresZona, Long> {

	public List<VincSectoresZona> findByZona(Zona zonaBean);

}
