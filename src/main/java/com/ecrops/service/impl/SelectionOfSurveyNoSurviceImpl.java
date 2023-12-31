package com.ecrops.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecrops.repo.SelectionOfSurveyRepo;
import com.ecrops.service.SelectionOfSurveyNoSurvice;


@Service("SelectionOfSurveyNoSurvice")
public class SelectionOfSurveyNoSurviceImpl implements SelectionOfSurveyNoSurvice{

	
	@Autowired
	private SelectionOfSurveyRepo repo;
	@Override
	public String getData(HttpServletRequest request, HttpSession session) {
		  String sesdcode = session.getAttribute("dcode").toString();
		  String sesmcode = session.getAttribute("mcode").toString();
		   System.out.println("sescode"+sesdcode);
		   System.out.println("sesmcode"+sesmcode);
		  String user = session.getAttribute("userid").toString();
		  System.out.println("user"+user);
		 String sesvcode = session.getAttribute("village").toString();
		  System.out.println("sesvcode"+sesvcode);
		  String data = "", cropyear = "", season = "", searchParam = "1", surveyNo = "", aadharNo = "", khataNo = "";
		  String qry = "", vcode = "", wbdcode = "", prejcode = "";
		// String activeYear = session.getAttribute("ACTIVEYEAR").toString(); //System.out.println("actvie yar:"+activeYear);
		 String activeYear = "2023";
         String crpses = request.getParameter("crYear"); //System.out.println("crpses:"+crpses);
         cropyear = crpses.split("@")[1];
         season = crpses.split("@")[0];// System.out.println("season:"+season);
         String tseason = "", fromKhata = "", toKhata = "";
         String dist = session.getAttribute("dcode").toString();
       //  wbdcode = util.MasterFunctions.MasterFunction(dist, "wbdcode");
         if (wbdcode.length() == 1) {
             wbdcode = "0" + wbdcode;
         }
         wbdcode="01";
         vcode = request.getParameter("vcode");
         System.out.println("vcode"+vcode);

       //  searchParam = request.getParameter("searchParam");
         searchParam="1";
         fromKhata="10";
         toKhata="20";
//         fromKhata = request.getParameter("fromKhno");System.out.println("from khat:"+fromKhata);
//         toKhata = (request.getParameter("toKhno") != null && request.getParameter("toKhno") != "") ? request.getParameter("toKhno") : "" + fromKhata + "";
//System.out.println("to khata :"+toKhata);
         String fromWsrno = request.getParameter("surveyno") != null ? request.getParameter("surveyno") : "";
         String toWsrno = (request.getParameter("tosurveyno") != null && request.getParameter("tosurveyno") != "") ? request.getParameter("tosurveyno") : "" + fromWsrno + "";

         String crbookingnwbTab = "cr_booking_nwb";
         String pattadartable = "pattadarmast_wb_partition_" + season + wbdcode + cropyear;
         String crbookingTab = "cr_booking_partition_" + season + wbdcode + cropyear;
         String tab2 = "cr_booking_nwb";
         String efish_data = "cr_details_efish";

         if (activeYear.equals(cropyear) && season.equals("K")) {
             pattadartable = "ecrop" + activeYear + ".pattadarmast_wb_partition_" + season + wbdcode + cropyear;
             crbookingTab = "ecrop" + activeYear + "." + crbookingTab;
             tab2 = "ecrop" + activeYear + "." + tab2;
             crbookingnwbTab = "ecrop" + activeYear + "." + crbookingnwbTab;
             efish_data = "ecrop" + activeYear + "." + efish_data;

         }

         String extAlreadyAlloted = "", wsnoAlready = "";
         
         
         
         
         
         
         
         
         tseason = "'" + season + "'";

         qry = " (select 'W' as data_src,rec_id,cr_dist_code,cr_mand_code,cr_vcode,cr_farmeruid,farmername,fathername,occupname,occupfname,kh_no,cr_sno,tot_extent,occup_extent,regno,"
                 + " sjointoccupant,cultivable_land,uncultivable_land,dcode,mcode,mobileno,part_key,cr_wsno  "
                 + " from " + pattadartable + " where status='Y' and cr_vcode=? and  ";

         if (searchParam.equals("1")) {
             qry += " cr_wsno between " + fromWsrno + " and " + toWsrno + "";
         } else if (searchParam.equals("2")) {
             qry += " kh_no between " + fromKhata + " and " + toKhata + "";
         }

         qry += " and  kh_no not in (select code from obj_unobj where trim(crb_remarks) in ('No')) "
                 + " and (cr_vcode,cr_sno,kh_no,occupname,occupfname,farmername,fathername,occup_extent,tot_extent,regno,sjointoccupant) "
                 + " not in (select cr_vcode,cr_sno,kh_no,occupname,occupfname,oc_name,oc_fname,occupant_extent,tot_extent,regno,sjointoccupant from " + crbookingTab + " where  vs_sel='Y' and cr_vcode=?) and "
                 + "  cast(cr_vcode as text)||cast(kh_no as text)||cr_sno not in (select cast(cr_vcode as text)||cast(kh_no as text)||cr_sno from  " + efish_data + " ) and"
                 + "  ((cr_vcode,cr_sno) in(select vcode,cr_sno from ecrop2023.rbk_surveyno_mapping where rbkuserid=? and dcode=? and mcode=? and cr_year=? and cr_season=? ))"
                 + " order by cr_wsno ) "
                 + " UNION  "
                 + " Select data_src,rec_id,cr_dist_code,cr_mand_code,cr_vcode,cr_farmeruid,oc_name,oc_fname,occupname,occupfname,kh_no,cr_sno,tot_extent,occupant_extent,0 as regno,"
                 + " 0 as sjointoccupant,0 as cultivable_land,0 as uncultivable_land,dcode,mcode,mobileno,part_key,cr_wsno from " + crbookingnwbTab + " where  " ;
               
                   if (searchParam.equals("1")) {
             qry += " cr_wsno between " + fromWsrno + " and " + toWsrno + "";
         } else if (searchParam.equals("2")) {
             qry += " kh_no between " + fromKhata + " and " + toKhata + "";
         }
                 
              qry+= "  and (cr_vcode,cr_sno,kh_no,occupname,occupfname ,oc_name,oc_fname,occupant_extent,tot_extent,regno,sjointoccupant,rec_id ) not in "
                 + "(select cr_vcode,cr_sno,kh_no,occupname,occupfname,oc_name,oc_fname,occupant_extent,tot_extent,regno,sjointoccupant,rec_id "
                 + " from " + crbookingTab + " where  vs_sel='Y' and cr_vcode=?) and "
                 + "  cast(cr_vcode as text)||cast(kh_no as text)||cr_sno not in (select cast(cr_vcode as text)||cast(kh_no as text)||cr_sno from " + efish_data + " ) "
                 + " and  cr_vcode=?  and vs_sel='Y' order by cr_wsno ";


              System.out.println(qry+"iiiiiiiiiiiiiiii");
              
        List<Object[]> dataList=   repo.saveEmployeeLogDetails(qry, sesvcode, user, sesdcode, sesmcode, cropyear, tseason);
           
        for(Object[] row : dataList) {
        	System.out.println(row[0].toString()+"jiiiiiiiiiiiiii");
        	
        }
        
		return wsnoAlready;

       //  String cntqry = "select count(cr_wsno) as srnocnt,sum(occupant_extent) as extent from " + crbookingTab + " where dcode=" + sesdcode + " and mcode=" + sesmcode + " and srno_userid='" + user + "' ";
      
//        if (rs1.next()) {
//             wsnoAlready = rs1.getString("srnocnt");
//             extAlreadyAlloted = rs1.getString("extent");
//
//      }

		
	} 


}
