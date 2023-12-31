package com.ecrops.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecrops.entity.ActiveSeasonEntity;
import com.ecrops.projection.ActiveSeasonProjections;

public interface ActiveSeasonRepository extends JpaRepository<ActiveSeasonEntity, Integer> {
	
	@Query(value = "select distinct on (a.cropyear, a.season) concat(a.season,'@',cropyear) as seasonvalue, concat(b.seasonname,'',cropyear) as cropyear from ecrop2023.activeseason a, ecrop2023.season b where a.season=b.season and a.active='A' and a.current_season='C' order by a.cropyear, a.season", nativeQuery = true)
	public List<ActiveSeasonProjections> getActiveSeason();

}
