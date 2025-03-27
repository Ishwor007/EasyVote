package com.E_Voting.Application.serviceimpl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.E_Voting.Application.models.Admin;
import com.E_Voting.Application.models.Candidate;
import com.E_Voting.Application.models.Voter;
import com.E_Voting.Application.repositories.AdminRepo;
import com.E_Voting.Application.repositories.CandidateRepo;
import com.E_Voting.Application.repositories.VoterRepo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailService implements UserDetailsService {
	@Autowired
	VoterRepo voterrepo;
	
	@Autowired
	AdminRepo adminrepo;
	
	@Autowired
	CandidateRepo candidaterepo;
	
	private Voter voter;
	
	private Admin admin;
	
	private Candidate candidate;
  
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		if (voterrepo.findByEmail(email)!= null) {
			this.voter=voterrepo.findByEmail(email);
			return new CustomUserDetail(voter);
		}
	 else if (adminrepo.findByEmail(email) != null){
           this.admin = adminrepo.findByEmail(email);
           return new CustomUserDetail(admin);
		} else if (candidaterepo.findByUname(email) != null){
	           this.candidate = candidaterepo.findByUname(email);
	           System.out.println("Load by username"+candidate.getName());
	           return new CustomUserDetail(candidate);
			}
	 else {
			throw new UsernameNotFoundException("user does not exist");
 
		}
	  
	   
	}

}