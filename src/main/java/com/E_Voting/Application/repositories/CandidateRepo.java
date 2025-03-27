package com.E_Voting.Application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.E_Voting.Application.models.Candidate;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate, Integer> {

	List<Candidate> findByLocation(String location);
	public Candidate findById(int id);
	
	@Query("SELECT DISTINCT c FROM Candidate c")
    List<Candidate> findAllUniqueCandidates();
	
	
	
	
	
	
}
