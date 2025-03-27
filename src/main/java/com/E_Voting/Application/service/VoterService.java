package com.E_Voting.Application.service;


import java.util.List;

import com.E_Voting.Application.models.Vote;
import com.E_Voting.Application.models.Voter;


public interface VoterService {
	public void saveVoter(Voter voter);
	public boolean checkEmail(String email);
	public void updateStatus(int id);

	List<Vote> findAllByUniqueVote();
	
	List<Voter> getAllVoter();
	
	
	
}
