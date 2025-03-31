package com.E_Voting.Application.serviceimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.E_Voting.Application.models.Admin;
import com.E_Voting.Application.models.Candidate;
import com.E_Voting.Application.repositories.AdminRepo;
import com.E_Voting.Application.repositories.CandidateRepo;
import com.E_Voting.Application.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	private static final String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int pwd_length = 10;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	AdminRepo adminrepo;
	
	@Autowired
	CandidateRepo candidaterepo;
	
	
	@Override
	public boolean saveAdmin(Admin admin, String email) {
		String plain_password = generatePassword();
		String encodedPassword = encodePassword(plain_password);
		sendAdminEmail(email, plain_password);// sending password email after regisrtating
        admin.setPassword(encodedPassword);
        admin.setRole("ADMIN");
        adminrepo.save(admin);		
        return true;
	}
	public String encodePassword(String plainPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode(plainPassword);
		return encoded;
	}
	@Override
	public String generatePassword() {
		StringBuilder sb = new StringBuilder();
		SecureRandom random = new SecureRandom();

		for (int i = 0; i < pwd_length; i++) {
			int randomIndex = random.nextInt(character.length());
			sb.append(character.charAt(randomIndex));
		}

		return sb.toString();
	}

	public void sendAdminEmail(String email, String plain_password) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("raayaseetal@gmail.com");
		message.setSubject("Admin Details");
		message.setText("The authentication details for admin is :" + email
				+ " password:" + plain_password + ". /nRegards:Ishwor Jung raya from easyvote project");
		mailSender.send(message);
	}
	@Override
	public void saveCandidate(@ModelAttribute("candidate") Candidate candidate,HttpServletRequest request,String password) throws IOException {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        MultipartFile partylogo = multipartRequest.getFile("party_logo");
        MultipartFile candidateimage = multipartRequest.getFile("candidate_image");
      
        String partylogoname = candidate.getName()+"_"+partylogo.getOriginalFilename();
        String candidatename =  candidate.getName()+"_"+candidateimage.getOriginalFilename();
        
        System.out.println(partylogoname);	
        System.out.println(candidatename);	
  
		byte[] partylogobytes = partylogo.getBytes();
		byte[] candidatenamebytes = candidateimage.getBytes();
		
        String path = "../E-VotingSystem/src/main/resources/static/candidatepicture/";
        
		Path partylogo_path = Paths.get(path + partylogoname);
		Files.write(partylogo_path, partylogobytes);
		
		Path candidatephoto_path = Paths.get(path + candidatename);
		Files.write(candidatephoto_path, candidatenamebytes);
		
		candidate.setLocation(candidate.getLocation().toLowerCase());
		candidate.setPartylogo(partylogoname);
		candidate.setCandidateimage(candidatename);
		candidate.setUname(candidate.getUname().toLowerCase());
		candidate.setPassword(password);
		System.out.println(partylogo_path);
		candidaterepo.save(candidate);
	}
}
