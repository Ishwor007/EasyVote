package com.E_Voting.Application.serviceimpl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.E_Voting.Application.models.Admin;
import com.E_Voting.Application.models.Voter;
import com.E_Voting.Application.repositories.AdminRepo;
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
	
	private Voter voter;
	
	private Admin admin;
  
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		if (voterrepo.findByEmail(email)!= null) {
			this.voter=voterrepo.findByEmail(email);
			return new CustomUserDetail(voter);
		}
	 else if (adminrepo.findByEmail(email) != null){
           this.admin = adminrepo.findByEmail(email);
           return new CustomUserDetail(admin);
		}else {
			throw new UsernameNotFoundException("user does not exist");
 
		}
	  
	   
	}

}