package com.E_Voting.Application.service;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.E_Voting.Application.models.Admin;
import com.E_Voting.Application.models.Candidate;


public interface AdminService {
  public boolean saveAdmin(Admin admin,String email);
  public void saveCandidate(Candidate candidate,HttpServletRequest request) throws IOException;
}
