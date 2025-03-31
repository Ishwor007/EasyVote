package com.E_Voting.Application.serviceimpl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.E_Voting.Application.models.Voter;
import com.E_Voting.Application.repositories.VoterRepo;


import java.io.IOException;
import java.util.Date;
import java.util.Random;


import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
     VoterRepo voterrepo;
	
	private String otp;
	@Autowired
	BCryptPasswordEncoder passwordencoder;

	public String sendEmail(String to) {
		SimpleMailMessage message = new SimpleMailMessage();
		otp = Integer.toString(generateOtp());
		
		message.setTo(to);
		message.setSubject("One Time Password");
		message.setText("Dear Sir/Madam,"
				+ "\n \nATTN : Please do not reply to this email.This mailbox is not monitored and you will not receive a response.\n"
				+ "\n \nYour One Time Password (OTP ) is " + otp + "."
				+ " If you have any queries, Please contact us at,\n" + "\n" + " EasyVote,\n"
				+ " guwarko,lalitpur, Nepal.\n" + " Phone # 977-98123456789\n"
				+ " Email Id: support@EasyVote.com.np\n" + " Warm Regards,\n" + " EasyVote.");
		mailSender.send(message);

		return otp;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	public String sendEmailHash(String to) {
		SimpleMailMessage message = new SimpleMailMessage();
		String otp = Integer.toString(generateOtp());
		String hashotp = hashOTP(otp);
		message.setTo(to);
		message.setSubject("One Time Password");
		message.setText("Dear Sir/Madam,"
				+ "\n \nATTN : Please do not reply to this email.This mailbox is not monitored and you will not receive a response.\n"
				+ "\n \nYour passcode to vote is " + hashotp + "."
				+ " If you have any queries, Please contact us at,\n" + "\n" + " EasyVote,\n"
				+ " guwarko,lalitpur, Nepal.\n" + " Phone # 977-98123456789\n"
				+ " Email Id: support@EasyVote.com.np\n" + " Warm Regards,\n" + " EasyVote.");

	


		mailSender.send(message);

		return hashotp;
	}
	
	public void sendConfimationEmail(String to,String transaction_id,long timestamp) {
		SimpleMailMessage message = new SimpleMailMessage();
		Date date = new Date(timestamp);
		message.setTo(to);
		message.setSubject("One Time Password");
		message.setText("Dear Sir/Madam,"
				+ "\n \nATTN : Please do not reply to this email.This mailbox is not monitored and you will not receive a response.\n"
				+ "\n \nYour transaction id is " + transaction_id + "."
				+ "\n \nYour timestamp is " + date + "."
				+ " If you have any queries, Please contact us at,\n" + "\n" + " EasyVote,\n"
				+ " guwarko,lalitpur, Nepal.\n" + " Phone # 977-98123456789\n"
				+ " Email Id: support@EasyVote.com.np\n" + " Warm Regards,\n" + " EasyVote.");

		mailSender.send(message);
	}
	
	
	public void sendCandidateEmail(String to,String password) {
		SimpleMailMessage message = new SimpleMailMessage();
		System.out.println("email-"+to);
       message.setTo(to);
		message.setSubject("Candidate Credentials");

       message.setText("Dear Sir/Madam,"
				+ "\n \nATTN : Please do not reply to this email.This mailbox is not monitored and you will not receive a response.\n"
				+ "\n \nYour Username id is " + to + "."
				+ "\n \nYour password is " + password + "."
				+ "\n \nPlease use this credentails to view your vote"
				+ " If you have any queries, Please contact us at,\n" + "\n" + " EasyVote,\n"
				+ " guwarko,lalitpur, Nepal.\n" + " Phone # 977-98123456789\n"
				+ " Email Id: support@EasyVote.com.np\n" + " Warm Regards,\n" + " EasyVote.");
       mailSender.send(message);
		
	}
	
    public int generateOtp() {
    	Random rand = new Random();
    	int randomNum = rand.nextInt(999999 - 111111 + 1) + 111111;
    	return randomNum;
    }
    
    private String hashOTP(String otp) {
		return DigestUtils.sha256Hex(otp); // Hash the OTP for storage and comparison
	}
    
//public void sendChangePasswordMail(String email) {
//		Voter voter = voterrepo.findByEmail(email);
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(email);
//		message.setSubject("Your EasyVote password has been changed");
//		message.setText("This is a confirmation that the password for"
//				+ " your EasyVote account having name "+voter.getFirst_name()+" "+voter.getLast_name()
//				+" has just been changed.");
//	    
//		mailSender.send(message);
//	}

}
