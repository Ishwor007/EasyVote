package com.E_Voting.Application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.E_Voting.Application.models.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer> {
	public Admin findByEmail(String email);

}
