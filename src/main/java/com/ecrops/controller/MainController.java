package com.ecrops.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecrops.dto.AuthenticationRequest;
import com.ecrops.entity.AppUser;
import com.ecrops.entity.CrEmpProfileEntity;
import com.ecrops.entity.Roles;
import com.ecrops.projection.ActiveSeasonProjections;
import com.ecrops.projection.EmployeeNames;
import com.ecrops.projection.VillageName;
import com.ecrops.repo.AppUserRepo;
import com.ecrops.repo.DistrictCsRepository;
import com.ecrops.repo.CrEmployeeProfileRepository;
import com.ecrops.repo.ActiveSeasonRepository;
import com.ecrops.repo.VillageSecDetRepository;
import com.ecrops.repo.VillSectionRepository;

@Controller
public class MainController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AppUserRepo appUsersRepo;

	@Autowired
	private VillageSecDetRepository villRepo;

	@Autowired
	private CrEmployeeProfileRepository empRepo;

	@Autowired
	private ActiveSeasonRepository cropYearRepo;

	@Autowired
	private VillSectionRepository villSecRepo;
	
	
	
	

	String email = "";
	AppUser user;

	@RequestMapping("/login")
	public String login(@ModelAttribute AuthenticationRequest authenticationRequest, Model model) {
		try {
			System.out.println("authenticate");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
		}
		return "login";
	}

	@RequestMapping("/")
	public String home(Model model, HttpSession session) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		email = authentication.getName();
		user = appUsersRepo.findByUserid(email);

		session.setAttribute("dcode", user.getDistrict());
		session.setAttribute("mcode", user.getBlockortehsil());
		session.setAttribute("village", user.getVillage());
		
		session.setAttribute("role", user.getType_user());
		session.setAttribute("userid", user.getUserid());

		Collection<Roles> roles = user.getRoles();
		roles.stream().forEach(a -> session.setAttribute("role", a.getName()));
		return "home";
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}

	@GetMapping("/employeeprofile")
	public String employeeprofile() {
		return "employeeprofile";
	}

	
//
//	@GetMapping("/allocOfSurveyNo")
//	public String allocOfSurveyNo(@ModelAttribute("allocSurvey") Employeename employeename, Model model,
//			HttpSession httpSession, HttpServletRequest httpServletRequest) {
//		String mcode = (String) httpSession.getAttribute("mcode");
//	
//		List<ActiveSeasonProjection> activeSeason = cropYearRepo.getActiveSeason();
//	               
//		
//		List<ActiveSeasonProjection> rbk = villSecRepo.getRbk(Integer.parseInt(mcode));
//
//		model.addAttribute("activeseason", activeSeason);
//		model.addAttribute("rbk", rbk);
//
//		return "AllocOfSurveyNo";
//	}
	
	
	
	

}
