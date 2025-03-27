package com.E_Voting.Application.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.E_Voting.Application.models.Admin;
import com.E_Voting.Application.models.Block;
import com.E_Voting.Application.models.Blockchain;
import com.E_Voting.Application.models.Candidate;
import com.E_Voting.Application.models.Vote;
import com.E_Voting.Application.models.Voter;
import com.E_Voting.Application.repositories.AdminRepo;
import com.E_Voting.Application.repositories.CandidateRepo;
import com.E_Voting.Application.repositories.VoteRepo;
import com.E_Voting.Application.repositories.VoterRepo;
import com.E_Voting.Application.service.CandidateService;
import com.E_Voting.Application.service.VoterService;
import com.E_Voting.Application.serviceimpl.BlockchainService;
import com.E_Voting.Application.serviceimpl.EmailService;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@Controller
public class VoterController {

	@Autowired
	private VoterRepo voterrepo;
	
	@Autowired
	private VoteRepo voterepo;
	
	@Autowired
	private AdminRepo adminrepo;
	
	
	@Autowired
	private CandidateRepo candidaterepo;
	
	@Autowired
    BlockchainService blockchainservice;
	
	@Autowired
	BCryptPasswordEncoder passwordencoder;
	@Autowired
	EmailService emailservice;
	@Autowired
	VoterService voterservice;
	
	@Autowired
	CandidateService candidateservice;
	
	

	
	private Map<String, String> otpstore = new HashMap<>();
	private List<Voter> voterstore =  new ArrayList<>();

	private List<Block> blocklist = new ArrayList<>();
	private Map<String, String> hashstore = new HashMap<>();

	
	@GetMapping("/register")
	public String getSignupPage() {
		return "signup";
	}
	


 @PostMapping("/register")
    public String saveVoter(@ModelAttribute("voter") Voter voter, BindingResult result,RedirectAttributes redirectAttributes) {
                    voterstore.add(voter);
                     if (result.hasErrors()) {
                   return "signup";
         }
              boolean hasvoterid = false;
              boolean email_check = voterservice.checkEmail(voter.getEmail());
              
              List<Voter> voterlist = voterservice.getAllVoter();
              for(Voter voters:voterlist) {
            	  if(voters.getVoter_id().equalsIgnoreCase(voter.getVoter_id())) {
            		  hasvoterid= true;
            	  }
              }
              if(email_check||hasvoterid) {
            	  return "redirect:/login";
              }
                     
              voter.setPassword(passwordencoder.encode(voter.getPassword()));
              String otp = emailservice.sendEmail(voter.getEmail());

              otpstore.put(voter.getEmail(), hashOTP(otp));
              
              
              redirectAttributes.addFlashAttribute("email", voter.getEmail());
              return "redirect:/otpverify";
 
 		
 	}
 @GetMapping("/otpverify")
	public String otpVerify(Model model,RedirectAttributes redirectAttributes) {
  	String email = (String) model.asMap().get("email");
  	String message = (String) model.asMap().get("message");
  	redirectAttributes.addFlashAttribute("email", email);
  	model.addAttribute("email", email);
  	model.addAttribute("message",message );
       return "otp";
}
 
 
	@PostMapping("/otpverify")
	public String otpVerify(@RequestParam("otp") int otp,
			@RequestParam("email") String email,
			RedirectAttributes redirectAttributes) {
		boolean result = voterservice.checkEmail(email);
		String useremail = email;

		String userotp = Integer.toString(otp);

		if (otpstore.containsKey(email)) {
			String storedOTP = otpstore.get(useremail);
			if (storedOTP.equals(hashOTP(userotp))) {
				if (result) {
					redirectAttributes.addFlashAttribute("email", email);
					System.out.println("inside result");
					return "redirect:/changepassword";
				}
				for (Voter voter : voterstore) {
					if (voter.getEmail().equals(email)) {
						voterservice.saveVoter(voter);
						otpstore.remove(email);
						return "redirect:/login";
					}
					otpstore.remove(email);
				
				}

				return "signup";
			}
		}
		redirectAttributes.addFlashAttribute("email", email);
		redirectAttributes.addFlashAttribute("message", "Invalid OTP. Please try again.");
		return "redirect:/otpverify";
	}

	
	@GetMapping("/login")
	public String loginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null|| authentication.getName().equals("anonymousUser")) {
			return "login";
		}else {
			return "redirect:/voter/voterdashboard";
		}
	}
	
	
	@GetMapping("/voter/voterdashboard")
	public String getVoterDashboard(Model model) {
		Voter voter = getCurrentUser();
		if(voter.getRole().equals("ADMIN")) {
			return "dashboard";
		}
		String location = voter.getLocation().toLowerCase();
		String name = voter.getFirst_name();

		List<Candidate> candidatelist = candidateservice.getCandidate(location);
		System.out.println(candidatelist);
		
		
		candidatelist.forEach(candidate->System.out.println(candidate.getName()));
		model.addAttribute("voter", voter);
		model.addAttribute("name", name);
		model.addAttribute("location", location);
		model.addAttribute("candidatelist", candidatelist);
		return "voterdashboard";
		
		
	}
	

	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession(false).invalidate();

		return "redirect:/signup";
	}
	
	@GetMapping({"/voters/candidate","/voters/candidate/{optional}"})
	public String getVote(@PathVariable(required = false) String optional,Model model,RedirectAttributes redirectAttributes) {
		this.blocklist=blockchainservice.initBlockchain();
	 
		 List<Boolean> validationResults = new ArrayList<>(Collections.nCopies(blocklist.size(), true));

		    boolean tampered = false; // Flag to track if a block is invalid

		    for (int i = 2; i < blocklist.size(); i++) {
		        Block currentBlock = blocklist.get(i);
		        Block previousBlock = blocklist.get(i - 1);

		        if (tampered || !currentBlock.getPreviousHash().equals(previousBlock.getHash()) 
		                      || !currentBlock.getHash().equals(currentBlock.calculateHash())) {
		            tampered = true;
		            validationResults.set(i, false);
		        }
		    }
	    model.addAttribute("blocklist", blocklist);
	    if(optional==null) {
		model.addAttribute("name", getCurrentUser().getFirst_name());
	    }else {
	    	model.addAttribute("name", "Admin");
	    }
	    model.addAttribute("validationResults", validationResults);

		return "blockchain";
	}
	
	
	@PostMapping("/voters/candidate")
	public String getVote(@RequestParam("id") int id, Vote vote, Model model,RedirectAttributes redirectAttributes) {
		Voter voter = getCurrentUser();
		System.out.println("id from vote-------------"+ id);
        String name = voter.getFirst_name();
		if(voter.isHasVoted()) {
			return "redirect:/voter/voterdashboard";
		}
		Candidate candidate = candidaterepo.findById(id);
		vote.setVoter(voter);
		vote.setCandidate(candidate);
           
		 String hash = emailservice.sendEmailHash(voter.getEmail());
         hashstore.put(voter.getEmail(), hash);		
         redirectAttributes.addFlashAttribute("email", voter.getEmail());
         redirectAttributes.addFlashAttribute("candidateId",candidate.getId());
         redirectAttributes.addFlashAttribute("vote", vote);
         redirectAttributes.addFlashAttribute("id",String.valueOf(id));
		return "redirect:/voter/hashverify";
	}

	
	@GetMapping("/voter/hashverify")
	public String hashVerify(@ModelAttribute("vote") Vote vote,Model model,RedirectAttributes redirectAttributes) {
		
		this.blocklist=blockchainservice.initBlockchain();
		List<Boolean> validationResults = new ArrayList<>();

		 for (int i = 0; i < blocklist.size(); i++) {
		        if (i == 0) {
		            validationResults.add(true); 
		            continue;
		        }else  if (i == 1) {
		            validationResults.add(true); 
		            continue;
		        }

		        Block currentBlock = blocklist.get(i);
		        Block previousBlock = blocklist.get(i - 1);

		        boolean isValid = currentBlock.getPreviousHash().equals(previousBlock.getHash())
		                          && currentBlock.getHash().equals(currentBlock.calculateHash());
		     if(!isValid) {
			        validationResults.add(isValid);
			        model.addAttribute("blocklist", blocklist);
					model.addAttribute("name", getCurrentUser().getFirst_name());
				    model.addAttribute("validationResults", validationResults);
                   return "blockchain";
		     }

		    }
  	String email = (String) model.asMap().get("email");
  	String message = (String) model.asMap().get("message");
  	String id = (String) model.asMap().get("id");

  	int candidate_id = Integer.parseInt(id);
  	model.addAttribute("email", email);
  	model.addAttribute("message",message );
  	model.addAttribute("candidateId",candidate_id);
  	redirectAttributes.addFlashAttribute("email", email);
  	redirectAttributes.addFlashAttribute("vote", vote);
       return "validate";
}
	
	@PostMapping("/voter/hashverify")
	public String hashVerify(@ModelAttribute("vote") Vote vote,@RequestParam("otp") String hash,
			@RequestParam("email") String email,@RequestParam("candidateId") int candidateId,
			RedirectAttributes redirectAttributes,Model model) {
		
			
		
		String transation_id="";
		long timestamp=0;
		Voter currentvoter = getCurrentUser();
		voterstore.add(currentvoter);

        String name = currentvoter.getFirst_name();
		boolean result = voterservice.checkEmail(email);
		String useremail = email;
		if (hashstore.containsKey(email)) {
			String storedOTP = hashstore.get(useremail);
			if (storedOTP.equals(hash)) {
				System.out.println(storedOTP);
				for (Voter voter : voterstore) {
					if (voter.getEmail().equals(email)) {
						hashstore.remove(email);
						Candidate candidate = candidaterepo.findById(candidateId);
						vote.setVoter(voter);
						vote.setCandidate(candidate);
						voterepo.save(vote);
						int totalvotes = candidate.getTotalvotes();
						totalvotes+=1;
			
						candidate.setTotalvotes(totalvotes);
						candidaterepo.save(candidate);
						currentvoter.setHasVoted(true);
						voterrepo.save(currentvoter);
						
						 this.blocklist = blockchainservice.addVote(vote);
						 
						 for(Block block:blocklist) {	 
						  transation_id = blockchainservice.returnTransactionId(block.getTransactionId());
						  timestamp = blockchainservice.returnTimestampDetails(block.getTimestamp());
						  System.out.println(transation_id);
						 }
						emailservice.sendConfimationEmail(email, transation_id, timestamp);
						model.addAttribute("name", name);
				        model.addAttribute("blocklist", blocklist);
				        
				        

						return "redirect:/voters/candidate";
					}
					otpstore.remove(email);
				
				}
       
				return "redirect:/voter/voterdashboard";
			}
		}
		System.out.println("outside the checking");
		redirectAttributes.addFlashAttribute("email", email);
		redirectAttributes.addFlashAttribute("message", "Invalid OTP. Please try again.");
		redirectAttributes.addFlashAttribute("id",String.valueOf(candidateId));
		return "redirect:/voter/hashverify";
	}
	
	
	
	
	
	
	
	@GetMapping("/voter/changepassword")
	public String changePassword(Model model) {
		Voter currentvoter = getCurrentUser();
       model.addAttribute("email", currentvoter.getEmail());
       model.addAttribute("name", currentvoter.getFirst_name());
		return "changepassword";
	}
	
	
	@PostMapping("/voter/changepassword")
	public String changePassword(@RequestParam("email") String email,
			                     @RequestParam("currentPassword") String currentpassword,
			                     @RequestParam("newPassword") String password,
			                     Model model) {
		
		System.out.println(currentpassword);
		Voter voter = getCurrentUser();
		String oldpassword = voter.getPassword();
				
		if(passwordencoder.matches(currentpassword, oldpassword)) {
			System.out.println("old password same");
			voter.setPassword(passwordencoder.encode(password));
			voterrepo.save(voter);

		}
	
		
		return "/voter/voterdashboards";
	}
	
	
	
	

	
	public Voter getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			// Assuming you have a UserRepository to retrieve the user by username
			return voterrepo.findByEmail(userDetails.getUsername());
		}
		return null; 
	}
	public Admin getCurrentAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			// Assuming you have a UserRepository to retrieve the user by username
			return adminrepo.findByEmail(userDetails.getUsername());
		}
		return null; 
	}


	private String hashOTP(String otp) {
		return DigestUtils.sha256Hex(otp); // Hash the OTP for storage and comparison
	}
	
	
}
