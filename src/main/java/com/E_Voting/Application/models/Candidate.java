package com.E_Voting.Application.models;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "candidate")
public class Candidate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String name;
    @Column
    private String partyAffiliation;
    @Column
    private String biography;
    @Column
	private String partylogo;
    @Column
    private String candidateimage;
    @Column
    private String location;
    @Column
    private int totalvotes;
    
    @OneToMany(mappedBy = "candidate")
    private List<Vote> votes;
    
    public Candidate() {
    	this.totalvotes=0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartyAffiliation() {
        return partyAffiliation;
    }

    public void setPartyAffiliation(String partyAffiliation) {
        this.partyAffiliation = partyAffiliation;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

	public String getPartylogo() {
		return partylogo;
	}

	public void setPartylogo(String partylogo) {
		this.partylogo = partylogo;
	}

	public String getCandidateimage() {
		return candidateimage;
	}

	public void setCandidateimage(String candidateimage) {
		this.candidateimage = candidateimage;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getTotalvotes() {
		return totalvotes;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public void setTotalvotes(int totalvotes) {
		this.totalvotes = totalvotes;
	}

   

  
}

