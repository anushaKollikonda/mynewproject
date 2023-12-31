package com.ecrops.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecrops.entity.VillageSecDetEntity;
import com.ecrops.projection.ActiveSeasonProjections;

public interface VillSectionRepository extends JpaRepository<VillageSecDetEntity, Integer> {
	

	@Query(value = "select distinct vcode,vname from ecrop2023.vill_sec_det where vcode in (select rbkcode from ecrop2023.emp_rbk_map where mcode=:mcode) order by vname", nativeQuery = true)
	public List<ActiveSeasonProjections> getRbk(@Param("mcode") Integer mcode);
	
	
	


}
