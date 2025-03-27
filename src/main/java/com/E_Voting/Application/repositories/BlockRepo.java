package com.E_Voting.Application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.E_Voting.Application.models.Block;

@Repository
public interface BlockRepo extends JpaRepository<Block, Long> {
    
	@Query(value = "SELECT * FROM block ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Block findLastBlock();
	
	List<Block> findAllByOrderByIdAsc();
}


