package com.E_Voting.Application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.E_Voting.Application.models.Vote;

@Repository
public interface VoteRepo extends JpaRepository<Vote, Integer>{
	
	@Query("SELECT DISTINCT v From Vote v")
	List<Vote> findAllByUniqueVote();
	
	

}
