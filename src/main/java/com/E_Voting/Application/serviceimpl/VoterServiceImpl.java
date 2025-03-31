package com.E_Voting.Application.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.E_Voting.Application.models.Vote;
import com.E_Voting.Application.models.Voter;
import com.E_Voting.Application.repositories.VoteRepo;
import com.E_Voting.Application.repositories.VoterRepo;
import com.E_Voting.Application.service.VoterService;

@Service
public class VoterServiceImpl implements VoterService {
	@Autowired
	VoterRepo voterrepo;
	@Autowired
	VoteRepo voterepo;
	@Override
	public void saveVoter(Voter voter) {
		voterrepo.save(voter);
		
	}
	@Override
	public boolean checkEmail(String email) {
		Voter voter = voterrepo.findByEmail(email);
		if(voter==null) {
			return false;
		}
		return true;
	}

	@Override
	public void updateStatus(int id) {
              voterrepo.updatestatus(id);		       
	}

	@Override
	public List<Vote> findAllByUniqueVote() {
		return voterepo.findAllByUniqueVote();
	}
	@Override
	public List<Voter> getAllVoter() {
		// TODO Auto-generated method stub
		return voterrepo.findAll();
	}
	
	
}
