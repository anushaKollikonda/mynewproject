package com.ecrops.repo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class PattadarDetailsRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public List<Object[]> getPattadarDetails(String wbdcode, String season, String cropyear, String bkIdList) {
		
		String activeYear = "2023";
		if (wbdcode.length() > 0) {
			wbdcode = "0" + wbdcode;
		}
		String tbname1 = "pattadarmast_wb_partition_" + season + wbdcode + cropyear;
		String crbookingNwb = "cr_booking_nwb";

		if (activeYear.equals(cropyear)) {
			tbname1 = "ecrop" + activeYear + "." + tbname1;
			crbookingNwb = "ecrop" + activeYear + "." + crbookingNwb;
		}
		String insertQry = null;
		
			insertQry = "select dcode,mcode,cr_vcode,cropyear,season,"
					+ " kh_no,cr_sno,tot_extent,occup_extent,cr_wsno "
					+ " from "
					+ tbname1 + "  where  rec_id=?";
		

		
		Query sql = entityManager.createNativeQuery(insertQry);
		sql.setParameter(1, Integer.parseInt(bkIdList));
		List<Object[]> executeUpdate = sql.getResultList();

		return executeUpdate;

	}

	@Transactional
	public int savePattadarDetails(String partkey, Integer dcode, Integer mcode, Integer vcode, Integer cropyear,
			String season, BigDecimal khathano, String surveyno, BigDecimal totalExtent, BigDecimal occupExtent,
			String updatedBy, Integer rbkcode, Integer empCode, BigInteger wsno, String rbkuserid, String datsrc,
			String recordId) {

		String insertQry = "insert into ecrop2023.rbk_surveyno_mapping(partkey,dcode,mcode,vcode,cr_year,"
				+ "cr_season,kh_no,cr_sno,tot_extent,occup_extent,updatedby,updateon,rbkcode,"
				+ "emp_code,cr_wsno,rbkuserid,data_src,rec_id)  values (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?)";
		Query sql = entityManager.createNativeQuery(insertQry);
		sql.setParameter(1, partkey);
		sql.setParameter(2, dcode);
		sql.setParameter(3, mcode);
		sql.setParameter(4, vcode);
		sql.setParameter(5, cropyear);
		sql.setParameter(6, season);
		sql.setParameter(7, khathano);
		sql.setParameter(8, surveyno);
		sql.setParameter(9, totalExtent);
		sql.setParameter(10, occupExtent);
		sql.setParameter(11, updatedBy);
		sql.setParameter(12, rbkcode);
		sql.setParameter(13, empCode);
		sql.setParameter(14, wsno);
		sql.setParameter(15, rbkuserid);
		sql.setParameter(16, datsrc);
		sql.setParameter(17, Integer.parseInt(recordId));

		int executeUpdate = sql.executeUpdate();
		return executeUpdate;

	}

	@Transactional
	public List<Object[]> crBookingNwb(String wbdcode, String season, String cropyear, String bkIdList) {
		String activeYear = "2023";
		if (wbdcode.length() > 0) {
			wbdcode = "0" + wbdcode;
		}
		String crbookingNwb = "cr_booking_nwb";

		if (activeYear.equals(cropyear)) {
			crbookingNwb = "ecrop" + activeYear + "." + crbookingNwb;
		}
		String insertQry = null;

		insertQry = "select dcode,mcode,cr_vcode,cr_year,cr_season,kh_no,cr_sno,occupant_extent as tot_extent ,"
				+ "occupant_extent, cr_wsno from " + crbookingNwb + " where rec_id=?";
		System.out.println("bkIdList"+bkIdList);

		Query sql = entityManager.createNativeQuery(insertQry);
		sql.setParameter(1, Integer.parseInt(bkIdList));
		List<Object[]> executeUpdate = sql.getResultList();
		System.out.println("executeUpdat" + executeUpdate.size());

		return executeUpdate;

	}

	@Transactional
	public int updateCrBookingNwd(Integer vcode, String bkdId) {
		String insertQry = null;

		insertQry = "update ecrop2023.cr_booking_nwb set vs_sel='Y' where cr_vcode=? and rec_id=?";

		Query sql = entityManager.createNativeQuery(insertQry);
		sql.setParameter(1, vcode);
		sql.setParameter(2, Integer.parseInt(bkdId));
		int executeUpdate = sql.executeUpdate();
		return executeUpdate;
	
	}

}
