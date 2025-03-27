package com.E_Voting.Application.serviceimpl;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.E_Voting.Application.models.Admin;
import com.E_Voting.Application.models.Candidate;
import com.E_Voting.Application.models.Voter;



@Service
public class CustomUserDetail implements UserDetails {
	String ROLE_PREFIX = "ROLE_";
	
	
	private Voter voter;
	private Candidate candidate;
	private Admin admin;
	CustomUserDetail() {

	}

	public CustomUserDetail(Voter u) {
		this.voter = u;
	}
	public CustomUserDetail(Admin u) {
		this.admin = u;
	}

	public CustomUserDetail(Candidate c) {
		this.candidate = c;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String role="";
		List<GrantedAuthority> authoritylist = new ArrayList<>();
		if(voter!=null&&voter.getRole().equals("VOTER")) {
		 role = ROLE_PREFIX+voter.getRole();
		 }else if(candidate!=null&&candidate.getRole().equals("CANDIDATE")){
				role = ROLE_PREFIX+candidate.getRole();
		
		}else{
			 System.out.println("at collection"+admin.getRole());

			 role = ROLE_PREFIX+admin.getRole();
			 

		}
		authoritylist.add(new SimpleGrantedAuthority(role));
		return authoritylist;
	}

	@Override
	public String getPassword() {
		if(voter!=null&&voter.getRole().equals("VOTER")) {
		return voter.getPassword();
		}else if(candidate!=null&&candidate.getRole().equals("CANDIDATE")) {
			return candidate.getPassword();
		}else {
			return admin.getPassword();
		}
	}

	@Override
	public String getUsername() {
		if(voter!=null&&voter.getRole().equals("VOTER")) {
		return voter.getEmail();
		}else if(candidate!=null&&candidate.getRole().equals("CANDIDATE")) {
			return candidate.getUname();
		}else {
			return admin.getEmail();

		}
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	public String getRole(){
		if(voter!=null&&voter.getRole().equals("VOTER")) {
		return voter.getRole();
		}else if(candidate!=null&&candidate.getRole().equals("CANDIDATE")) {
			return candidate.getRole();
		}else {
			return admin.getRole();
		}
	}
	public String getFname() {
		return voter.getFirst_name();
	}
	public String getLname() {
		return voter.getLast_name();
	}
}
