package com.E_Voting.Application.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.E_Voting.Application.models.Candidate;
import com.E_Voting.Application.repositories.CandidateRepo;
import com.E_Voting.Application.service.CandidateService;

@Service
public class CandidateServiceImpl implements CandidateService {
	
	@Autowired
	CandidateRepo candidaterepo;
	

	@Override
	public List<Candidate> getCandidate(String location) {
		
		return candidaterepo.findByLocation(location) ;
	}

	@Override
	public List<Candidate> getAllUniqueCandidates() {
		
 		return candidaterepo.findAllUniqueCandidates();
	}
	@Override
	public Candidate getCandidateByID(int id) {
		Candidate candidate  = candidaterepo.getById(id);
		return candidate;
	}

	
	
}
