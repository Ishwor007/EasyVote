package com.E_Voting.Application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.E_Voting.Application.serviceimpl.CustomUserDetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                    Authentication authentication) throws IOException {

	    CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

	    if (userDetail.getRole().equals("ADMIN")) {
	        response.sendRedirect("/admin/dashboard"); 
	    } else if (userDetail.getRole().equals("VOTER")) {
	        response.sendRedirect("/voter/voterdashboard"); 
	    } else if(userDetail.getRole().equals("CANDIDATE")) {
	        response.sendRedirect("/candidate/viewvotes");

	    }
	}

}
