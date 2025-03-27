package com.E_Voting.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.E_Voting.Application")
@SpringBootApplication
public class EVotingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EVotingSystemApplication.class, args);
	}

}
