package com.E_Voting.Application.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.E_Voting.Application.models.Admin;
import com.E_Voting.Application.models.Candidate;
import com.E_Voting.Application.models.Vote;
import com.E_Voting.Application.models.Voter;
import com.E_Voting.Application.repositories.CandidateRepo;
import com.E_Voting.Application.service.AdminService;
import com.E_Voting.Application.service.CandidateService;
import com.E_Voting.Application.service.VoterService;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminservice;
	
	@Autowired
	CandidateService candidate_service;
	

	
	@Autowired
	VoterService voter_Service;
	  @GetMapping("/signup/admin")
      public String getAdminSignupPage() {
      	return "adminsignup";
      	
	  }
      	@PostMapping("/saveadmin")
        public String saveAdminDetails(@ModelAttribute("user") Admin admin, @RequestParam("email") String email) {
        	boolean result=adminservice.saveAdmin(admin, email);
        	return "redirect:/signup/admin"; 
        
      }
      	
      	@GetMapping("/admin/addcandidate")
      	public String addCandidate() {
      		return "addcandidateform";
      	}
      	
      	@PostMapping("/admin/addcandidate")
      	public String addCandidate(@ModelAttribute("candidate") Candidate candidate,HttpServletRequest request) {
      		try {
      		adminservice.saveCandidate(candidate,request);
      		}catch(Exception e) {
      			System.out.println(e.getMessage());
      		}

      		return "addcandidateform";
      	}
      	
      	@GetMapping("/admin/dashboard")
    	public String getAdminDashboard(Model model) {
      		List<Candidate> candidateslist = candidate_service.getAllUniqueCandidates();
      		
      		List<Vote> votelist = voter_Service.findAllByUniqueVote();
      		
      		List<Voter> totalvoters = voter_Service.getAllVoter();
      		model.addAttribute("totalvoters", totalvoters.size());
      		model.addAttribute("candidateno", candidateslist.size());
      		model.addAttribute("voteno", votelist.size());
      		model.addAttribute("candidates", candidateslist);
      		
      		
    		return "dashboard";
    	}
      	
      	@GetMapping("/admin/managecandidate")
      	public String editCandidate(Model model) {
      	List<Candidate> candidatelist = candidate_service.getAllUniqueCandidates();
      	model.addAttribute("candidatelist", candidatelist);
      		return "candidatelist";
      	}
      	
      	
      	
      	@PostMapping("/admin/editcandidate/{id}")
      	public String editCandidate(@PathVariable("id") int id, Model model) {
      		Candidate candidate = candidate_service.getCandidateByID(id);
      		model.addAttribute("candidate", candidate);
      		return "addcandidateform";
      	}
      	@GetMapping("/admin/deletecandidate")
      	public void deleteCandidate() {
      		
      	}

}
