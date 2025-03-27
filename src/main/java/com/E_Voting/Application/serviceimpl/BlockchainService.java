package com.E_Voting.Application.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.E_Voting.Application.models.Block;
import com.E_Voting.Application.models.Blockchain;
import com.E_Voting.Application.models.Vote;
import com.E_Voting.Application.repositories.BlockRepo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;



@Service
public class BlockchainService {
	
    private List<Block> blocklist;

	
	@Autowired
    private BlockRepo blockRepository;
	
	private Blockchain blockchain;

    
    public List<Block> initBlockchain() {
        blockchain = new Blockchain();

        List<Block> blocks = blockRepository.findAllByOrderByIdAsc();  // Ensure blocks are ordered by ID
        for (Block block : blocks) {
            blockchain.addBlockFromDatabase(block);
        }
       this.blocklist= blockchain.getChain();
       
       for (Block block:blocklist) {
    	   System.out.println("block from database-"+block.getTransactionId());
       }
       return blocklist;
    }

    public boolean hasUserVoted(String voterId) {
        return blockRepository.findAll()
                .stream()
                .anyMatch(block -> block.getVoterId().equals(voterId));
    }

    @Transactional
    public synchronized List<Block> addVote(Vote vote) {
        Block lastBlock = blockRepository.findLastBlock();
        String previousHash = (lastBlock == null) ? "0" : lastBlock.getHash();

        
         String voterId = vote.getVoter().getVoter_id();
         String candidateid =String.valueOf(vote.getCandidate().getId()) ;
         
         String hashvoterid = applySHA256(voterId);
         String hashcandidateid = applySHA256(candidateid);
         
         
        Block newBlock = new Block(hashvoterid, hashcandidateid, previousHash);
        blockRepository.save(newBlock);
        List<Block> blocklist = initBlockchain();
        
        return  blocklist;
    }
	
    public boolean isValid() {
    	initBlockchain();

        for (int i = 2; i < blocklist.size(); i++) {
            Block currentBlock = blocklist.get(i);
            Block previousBlock = blocklist.get(i - 1);

        	System.out.println("is valid section- "+previousBlock.getHash());
        	System.out.println("is valid section- "+currentBlock.getPreviousHash());


            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }
	
    private String applySHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        
    }
        public String returnTransactionId(String TransactionId) {
      
        	return TransactionId;
        }
        
        public long returnTimestampDetails(long timestamp) {
        	return timestamp;
        }
    
}
