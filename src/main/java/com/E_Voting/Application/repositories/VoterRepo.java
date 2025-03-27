package com.E_Voting.Application.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.E_Voting.Application.models.Voter;

@Repository
public interface VoterRepo extends JpaRepository<Voter, Integer> {

	public Voter findByEmail(String email);
	
	@Modifying
    @Transactional
    @Query("UPDATE Voter v SET v.hasVoted = true WHERE v.id = :id")
    int updatestatus(int id);
	
	
	
	
	
}
