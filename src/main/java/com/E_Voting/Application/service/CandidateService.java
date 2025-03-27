package com.E_Voting.Application.service;

import java.util.List;

import com.E_Voting.Application.models.Candidate;


public interface CandidateService {

	public List<Candidate> getCandidate(String location);
	
	public List<Candidate> getAllUniqueCandidates();
    public Candidate getCandidateByID(int id);
}
