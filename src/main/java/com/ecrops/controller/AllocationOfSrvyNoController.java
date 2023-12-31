package com.ecrops.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecrops.dto.PattadharPojo;

import com.ecrops.entity.DistrictCsEntity;
import com.ecrops.entity.CrEmpProfileEntity;
import com.ecrops.entity.MandalCsEntity;

import com.ecrops.entity.VillageCsEntity;
import com.ecrops.entity.WbVillageMastEntity;
import com.ecrops.projection.ActiveSeasonProjections;

import com.ecrops.projection.RbkDetailsProjection;

import com.ecrops.repo.DatabaseRepo;
import com.ecrops.repo.DistrictCsRepository;

import com.ecrops.repo.ActiveSeasonRepository;
import com.ecrops.repo.MandalCsRepository;
import com.ecrops.repo.PattadarDetailsRepository;

import com.ecrops.repo.VillSectionRepository;
import com.ecrops.repo.VillagesCsRepository;
import com.ecrops.repo.WbvillagesRepository;
import com.ecrops.service.AllocationService;

@Controller
public class AllocationOfSrvyNoController {

	@Autowired
	private AllocationService employeeService;

	
	
	@Autowired
	private DatabaseRepo repo;

	@Autowired
	private PattadarDetailsRepository detailsRepository;

	@Autowired
	private VillagesCsRepository villageyrepo;

	@Autowired
	private DistrictCsRepository districtRepo;

	@Autowired
	private MandalCsRepository mandalRepo;

	@Autowired
	private WbvillagesRepository repos;
	
	@Autowired
	private ActiveSeasonRepository cropYearRepo;

	@Autowired
	private VillSectionRepository villSecRepo;
	

	

	@GetMapping("/allocOfSurveyNo")
	public String allocOfSurveyNo(@ModelAttribute("allocSurvey") CrEmpProfileEntity employeename, Model model,
			HttpSession httpSession, HttpServletRequest httpServletRequest) {
		String mcode = (String) httpSession.getAttribute("mcode");
	
		List<ActiveSeasonProjections> activeSeason = cropYearRepo.getActiveSeason();
	               
		
		List<ActiveSeasonProjections> rbk = villSecRepo.getRbk(Integer.parseInt(mcode));

		model.addAttribute("activeseason", activeSeason);
		model.addAttribute("rbk", rbk);

		return "AllocOfSurveyNo";
	}
	
	
	

	

	@PostMapping("/getPattaDetails")
	public String pattadharProfile(HttpServletRequest httpServletRequest, HttpSession httpSession, Model model) {

		String dcode = (String) httpSession.getAttribute("dcode");
		String mcode = (String) httpSession.getAttribute("mcode");
		String userid = (String) httpSession.getAttribute("userid");

		String cropyear = "", season = "", empCode = "";
		String qry = "", vcode = "", wbdcode = "", rbkcode = "";
		empCode = httpServletRequest.getParameter("employee");
		vcode = httpServletRequest.getParameter("village");
		rbkcode = httpServletRequest.getParameter("rbk");
		wbdcode = repo.getWbdCode(dcode);
       
		System.out.println("dcode" + dcode);
		System.out.println("userid" + userid);
		System.out.println("mcode" + mcode);
		System.out.println("empcode" + empCode);

		System.out.println("vcode is" + vcode);
		// wbdcode = util.MasterFunctions.MasterFunction(dist, "wbdcode");
		String crpses = httpServletRequest.getParameter("crYear");
		cropyear = crpses.split("@")[1];
		season = crpses.split("@")[0];
		 String activeYear = cropyear;
		String crbooknwb = "cr_booking_nwb";
		String efishTab = "cr_details_efish";
		String partitionName = "pattadarmast_wb_partition_";
		String rbksrnoMapTab = "rbk_surveyno_mapping_";
		String partKey = "";

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

		List<PattadharPojo> pojo = repo.pattadharDetails(qry, partitionName, vcode, efishTab, rbksrnoMapTab, crbooknwb);
		System.out.println("mcoce--->" + Integer.parseInt(mcode));
		DistrictCsEntity dname = districtRepo.findByDcode(Integer.parseInt(dcode));
		MandalCsEntity mname = mandalRepo.findByMcode(Integer.parseInt(mcode));
		VillageCsEntity vname = villageyrepo.findByVcode(Integer.parseInt(vcode.trim()));
		WbVillageMastEntity vnmae = repos.findByWbvcode(Integer.parseInt(vcode.trim()));

		model.addAttribute("pattadharDetails", pojo);
		model.addAttribute("vcode", vcode);

		model.addAttribute("districtname", dname.getDname());
		model.addAttribute("mandalname", mname.getMname());
		model.addAttribute("villagename", vnmae.getWbvname());
		model.addAttribute("rbkcode", rbkcode);
		model.addAttribute("empcode", empCode);
		model.addAttribute("rbkcode", rbkcode);
		model.addAttribute("empcode", empCode);
		model.addAttribute("cropyear", cropyear);
		model.addAttribute("season", season);
		model.addAttribute("partkey", partKey);

		return "AllocSrvyDetails";
	}

	@PostMapping("/saveSelection")
	public String saveSelection(HttpServletRequest httpServletRequest, HttpSession httpSession,
			RedirectAttributes redirectAttributes) {
		List<String> selectedBkIds = null;
		Integer dcode = 0, mcode = 0, cr_vcode = 0, cryear = 0;
		String surveyNo = "";
		Character season1 = null;
		BigDecimal khathNo = null, totalextent = null, occupextent = null, cropYear = null;
		BigInteger wsno = null;
		String[] bkIdList = httpServletRequest.getParameterValues("selectedBkIds");
		String[] datasrc = httpServletRequest.getParameterValues("dataSrc");
		String cropyear = httpServletRequest.getParameter("pcropyear");
		System.out.println("cropyear" + cropyear);
		String season = httpServletRequest.getParameter("pcropseason");
		String rbkcode = httpServletRequest.getParameter("rbkcodes");
		System.out.println("rbkcode is vaa" + rbkcode);
		String vcode = httpServletRequest.getParameter("vcodes").trim();
		System.out.println("vcode is" + vcode);
		String empCode = httpServletRequest.getParameter("empCode").trim();
		String partkey = httpServletRequest.getParameter("ppartkey").trim();
		System.out.println("partkey" + partkey);
		// sesion related
		String userid = httpSession.getAttribute("userid").toString();
		String sesdcode = httpSession.getAttribute("dcode").toString();
		String sesmcode = httpSession.getAttribute("mcode").toString();
		String wbdcode = repo.getWbdCode(sesdcode);
       int count=0;
		List<RbkDetailsProjection> rbkUserId = employeeService.getRbkUserId(Integer.parseInt(rbkcode),
				Integer.parseInt(empCode));
		String rbkuserid2 = rbkUserId.get(0).getRbkuserid();
		if (bkIdList != null) {
			selectedBkIds = Arrays.asList(bkIdList);
		}

		for (int i = 0; i < bkIdList.length; i++) {

			if (datasrc[i].equals("W")) {
				List<Object[]> pattadarDetails = detailsRepository.getPattadarDetails(wbdcode, season, cropyear,
						bkIdList[i]);
				for (Object[] list : pattadarDetails) {
					Object[] value = list;
					dcode = (Integer) value[0];
					mcode = (Integer) value[1];
					cr_vcode = (Integer) value[2];
					cryear = (Integer) value[3];
					season1 = (Character) value[4];
					khathNo = (BigDecimal) value[5];
					surveyNo = (String) value[6];
					totalextent = (BigDecimal) value[7];
					occupextent = (BigDecimal) value[8];
					wsno = (BigInteger) value[9];
				}
				 count += detailsRepository.savePattadarDetails(partkey, dcode, mcode,
						Integer.parseInt(vcode), Integer.parseInt(cropyear), season, khathNo, surveyNo, totalextent,
						occupextent, userid, Integer.parseInt(rbkcode), Integer.parseInt(empCode), wsno, rbkuserid2,
						datasrc[i], bkIdList[i]);
			} else {
				List<Object[]> crBooking = detailsRepository.crBookingNwb(wbdcode, season, cropyear, bkIdList[i]);
				for (Object[] list : crBooking) {
					Object[] value = list;
					dcode = (Integer) value[0];
					mcode = (Integer) value[1];
					cr_vcode = (Integer) value[2];
					cropYear = (BigDecimal) value[3];
					season1 = (Character) value[4];
					khathNo = (BigDecimal) value[5];
					surveyNo = (String) value[6];
					totalextent = (BigDecimal) value[7];
					occupextent = (BigDecimal) value[8];
					wsno = (BigInteger) value[9];
				}
		 count+=detailsRepository.savePattadarDetails(partkey, dcode, mcode, Integer.parseInt(vcode),
						Integer.parseInt(cropyear), season, khathNo, surveyNo, totalextent, occupextent, userid,
						Integer.parseInt(rbkcode), Integer.parseInt(empCode), wsno, rbkuserid2, datasrc[i],
						bkIdList[i]);
				detailsRepository.updateCrBookingNwd(Integer.parseInt(vcode), bkIdList[i]);

			}
		}
		redirectAttributes.addFlashAttribute("msg",  count+"Successfully allocated survey numbers to VAA/VHA/VSA");
		return "redirect:/allocOfSurveyNo";
	}

}
