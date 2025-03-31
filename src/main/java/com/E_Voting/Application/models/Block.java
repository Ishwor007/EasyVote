package com.E_Voting.Application.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "block")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String transactionId;
    private String hash;
    private String previousHash;
    private String voterId;
    private String candidateId;
    private long timestamp;
    private int nonce;
    public Block() {}
    public Block(String voterId, String candidateId, String previousHash) {
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.transactionId = generateTransactionId();  // Generate transaction ID
        this.hash = calculateHash();
    }

    public String generateTransactionId() {
        String data = voterId + candidateId + timestamp; 
        return applySHA256(data);  
    }
    public String calculateHash() {
        String data = previousHash + timestamp + voterId + candidateId +transactionId;
        return applySHA256(data);
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	public String getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

    
    
    }
