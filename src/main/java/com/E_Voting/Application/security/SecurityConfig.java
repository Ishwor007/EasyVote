package com.E_Voting.Application.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.E_Voting.Application.serviceimpl.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomUserDetailService customuserdetailservice;
	private final CustomAuthenticationSuccessHandler successHandler;
    public SecurityConfig(final CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		  
        http
                .authorizeHttpRequests(requests -> requests
                        .antMatchers("/login", "/register","/otpverify", "/voterdashboard",
                                "/", "/raisecapital", "/blockchain", "/addcandidate",
                                "/resetpassword", "/updatepassword", "/forgotpassword",
                                "/changepassword", "/reset/reports", "/company/documents/reports", "/signup/admin",
                                "/saveadmin")
                        .permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/voter/**").hasRole("VOTER") 
                        .antMatchers("/candidate/**").hasRole("CANDIDATE")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler)  // Use custom success handler
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/processlogin")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/logout"))
                .exceptionHandling(withDefaults());

	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customuserdetailservice)
				.passwordEncoder(passwordEncoder());

	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/resources/**", "/static/**", "/css/**", "/images/**", "/documents/**",
						"classpath:/static/candidatepicture/**");
	}

	}