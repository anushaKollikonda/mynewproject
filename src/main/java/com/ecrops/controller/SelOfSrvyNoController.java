package com.ecrops.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecrops.projection.ActiveSeasonProjections;
import com.ecrops.repo.ActiveSeasonRepository;
import com.ecrops.repo.VillSectionRepository;
import com.ecrops.repo.VillageSecRevRepository;
import com.ecrops.service.SelectionOfSurveyNoSurvice;


@Controller
public class SelOfSrvyNoController {
	@Autowired
	private ActiveSeasonRepository cropYearRepo;

	@Autowired
	private VillSectionRepository villSecRepo;
	
	@Autowired
	private VillageSecRevRepository villageRevenueRepo;
	
	@Autowired
	private SelectionOfSurveyNoSurvice selectionService;
	
	
	
	@GetMapping("/selectionOfSurveyNo")
	public String SelectionOfSrveyNO(HttpSession httpSession, HttpServletRequest httpServelHttpServletRequest,Model model) {
		String vcode = (String) httpSession.getAttribute("village");
		System.out.println("my session vcode is"+vcode);
		
		List<ActiveSeasonProjections> activeSeason = cropYearRepo.getActiveSeason();
	    List<ActiveSeasonProjections> rbk = villageRevenueRepo.getVillageListByRbk(Integer.parseInt(vcode));
	    System.out.println("wbvcode"+rbk.get(0).getWbvname());

		model.addAttribute("activeseason", activeSeason);
		model.addAttribute("rbk", rbk);
		return "SelectionOfSuvrveyNo";
	}
	
	
	
	
	
	
}
