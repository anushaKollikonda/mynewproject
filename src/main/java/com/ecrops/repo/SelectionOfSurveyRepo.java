package com.ecrops.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;


import org.springframework.stereotype.Component;

@Component
public class SelectionOfSurveyRepo {

	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public List<Object[]> saveEmployeeLogDetails(String qry,String vcode,String user, String sesdcode, String sesmcode,String cropyear, String season) {
		System.out.println("qry"+qry);
   Query query=	(Query) entityManager.createNativeQuery(qry);
		 
		
   query.setParameter(1, Integer.parseInt(vcode));
   query.setParameter(2, Integer.parseInt(vcode));
   query.setParameter(3, user);
   query.setParameter(4, Integer.parseInt(sesdcode));
   query.setParameter(5, Integer.parseInt(sesmcode));
   query.setParameter(6, Integer.parseInt(cropyear));
   query.setParameter(7, season);
   query.setParameter(8, Integer.parseInt(vcode));

   query.setParameter(9, Integer.parseInt(vcode));
   System.out.println(qry+"uuuuuuuu");
	
	return query.getResultList();
	}}