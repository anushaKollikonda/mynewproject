package com.ecrops.repo;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.ecrops.dto.PattadharPojo;

import java.util.List;

@Repository
public class DatabaseRepo {

	@PersistenceContext
	EntityManager entityManager;
	
	public List<PattadharPojo> methods(String wbdcode, String cropyear,String season, String vcode) {
		List<PattadharPojo> l = new ArrayList<>();
		String qry=null;
		String crbooknwb = "cr_booking_nwb";
		String efishTab = "cr_details_efish";
		String partitionName = "pattadarmast_wb_partition_";
		String rbksrnoMapTab = "rbk_surveyno_mapping_";
		String partKey = "";
		String activeYear="2023";

		if (Integer.parseInt(wbdcode) <= 9) {
			partitionName = partitionName + season + "0" + wbdcode + cropyear;
			rbksrnoMapTab = rbksrnoMapTab + season + "0" + wbdcode + cropyear;
			partKey = season + "0" + wbdcode + cropyear;

		} else {
			partitionName = partitionName + season + wbdcode + cropyear;
			rbksrnoMapTab = rbksrnoMapTab + season + wbdcode + cropyear;
			partKey = season + wbdcode + cropyear;
		}

		if (activeYear.equals(cropyear)) {
			partitionName = "ecrop" + cropyear + "." + partitionName;
			rbksrnoMapTab = "ecrop" + cropyear + "." + rbksrnoMapTab;
			efishTab = "ecrop" + cropyear + "." + efishTab;
			crbooknwb = "ecrop" + cropyear + "." + crbooknwb;
		} else {
			partitionName = partitionName;
		}
		qry =  "(select rec_id as bookingid,'W',cr_wsno,cr_dist_code,cr_mand_code,cr_vcode,cr_farmeruid,"
				+ "farmername,fathername,mobileno,kh_no,cr_sno,tot_extent, occupname,occupfname,"
				+ "occup_extent  from " + partitionName + " where status='Y' and cr_vcode=" + vcode + " "
				+ "and  kh_no not in (select code from obj_unobj where trim(crb_remarks) in ('No'))  "
				+ " and (cr_sno,kh_no,cr_vcode) " + "not in (select cr_sno,kh_no,vcode from   " + rbksrnoMapTab
				+ " where vcode=" + vcode + " ) " + " order by cr_wsno,kh_no) " 
				+ "UNION "
				+ "select rec_id as bookingid,data_src, cr_wsno,cr_dist_code,cr_mand_code,cr_vcode, "
				+ "cr_farmeruid,oc_name,oc_fname,mobileno,kh_no,cr_sno,tot_extent, occupname,occupfname,"
				+ "occupant_extent from " + crbooknwb + " where cr_vcode=" + vcode + " and vs_sel is null"
				+ " order by cr_wsno,kh_no limit 5";
		//List<PattadharPojo> pojo = method(qry);
		   
	return  l;
	}
	

	public List<PattadharPojo> pattadharDetails(String var, String partitionName, String vcode, String efishTab, String rbksrnoMapTab, String crbooknwb) {
		
		var =  "(select rec_id as bookingid,'W',cr_wsno,cr_dist_code,cr_mand_code,cr_vcode,cr_farmeruid,"
				+ "farmername,fathername,mobileno,kh_no,cr_sno,tot_extent, occupname,occupfname,"
				+ "occup_extent  from " + partitionName + " where status='Y' and cr_vcode=" + vcode + " "
				+ " and  kh_no not in (select code from obj_unobj where trim(crb_remarks) in ('No')) "
				+ "and cast(cr_vcode as text)||cast(kh_no as text)||cr_sno not in "
				+ "(select cast(cr_vcode as text)||cast(kh_no as text)||cr_sno from " + efishTab + "  "
				+ "where cr_vcode=" + vcode + ") and (cr_sno,kh_no,cr_vcode) not in "
				+ "(select cr_sno,kh_no,vcode from   " + rbksrnoMapTab
				+ " where vcode=" + vcode + " ) " + " order by cr_wsno,kh_no) " 
				+ "UNION "
				+ "select rec_id as bookingid,data_src, cr_wsno,cr_dist_code,cr_mand_code,cr_vcode, "
				+ "cr_farmeruid,oc_name,oc_fname,mobileno,kh_no,cr_sno,tot_extent, occupname,occupfname,"
				+ "occupant_extent from " + crbooknwb + " where cr_vcode=" + vcode + " and vs_sel is null"
				+ " order by cr_wsno,kh_no limit 500";
		List<PattadharPojo> pojo = new ArrayList<>();
		
		Query query = entityManager.createNativeQuery(var);
		List<Object> objects = query.getResultList();

		if (objects != null && objects.size() > 0) {

			for (Object patta : objects) {

				Object[] row = (Object[]) patta;
				
				PattadharPojo pojos = new PattadharPojo();
				pojos.setDatasrc(row[1].toString());
				
				pojos.setSurveyno(row[11].toString());
		pojos.setWholesurveyno(row[2].toString());
				pojos.setKhathano(row[10].toString());
			pojos.setTotalextent(row[12].toString());
				
				pojos.setBkid(Integer.parseInt(row[0].toString()));

				pojo.add(pojos);
			}
		}

		return pojo;

	}

	public String getWbdCode(String dcode) {
		String query = "select wbdcode from ecrop2023.district_2011_cs where dcode=" + dcode + "";
		Query result = entityManager.createNativeQuery(query);
		Object object = result.getSingleResult();
		System.out.println(object.toString());
		return object.toString();
	}

	public int executeInsertQuery(String partKey, String dcode, String mcode, String crVcode, int crYear,
			String crSeason, String khNo, String crSno, double totExtent, double occupExtent, String updatedBy,
			String rbkCode, String empCode, String crWsno, String rbkUserId, String dataSource, long recId,
			String tbname1) {
		String query = "INSERT INTO ecrop2023.rbk_surveyno_mapping(partkey,dcode,mcode,vcode,cr_year,cr_season,kh_no,cr_sno,"
				+ "tot_extent,occup_extent,updatedby,updateon,rbkcode,emp_code,cr_wsno,rbkuserid,data_src,rec_id) "
				+ "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?, ? " + "FROM " + tbname1
				+ " WHERE rec_id = ?)";

		entityManager.createNativeQuery(query).setParameter(1, partKey).setParameter(2, dcode).setParameter(3, mcode)
				.setParameter(4, crVcode).setParameter(5, crYear).setParameter(6, crSeason).setParameter(7, khNo)
				.setParameter(8, crSno).setParameter(9, totExtent).setParameter(10, occupExtent)
				.setParameter(11, updatedBy).setParameter(12, Integer.parseInt(rbkCode))
				.setParameter(13, Integer.parseInt(empCode)).setParameter(14, crWsno).setParameter(15, rbkUserId)
				.setParameter(16, dataSource).setParameter(17, recId).executeUpdate();
		return 10;
	}

	public String getRbkUserid(String qry1) {
		Query query = entityManager.createNativeQuery(qry1);
		Object obj=  query.getSingleResult();
		return obj.toString();
	}

	public List<Object> getPattadharResult(String query) {
	
		  Query  result = entityManager.createNativeQuery(query);
		    return result.getResultList();
	}
	
	   
}
