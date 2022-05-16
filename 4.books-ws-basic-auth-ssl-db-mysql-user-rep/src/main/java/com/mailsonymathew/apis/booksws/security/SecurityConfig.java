package com.mailsonymathew.apis.booksws.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private BCryptPasswordEncoder bCyptPasswordEncoder;
	private UserDetailsServiceImpl userDetailsService;
	private BooksWsAuthneticationEntryPoint authenticationEntryPoint;
	
	
	public SecurityConfig(BCryptPasswordEncoder bCyptPasswordEncoder, UserDetailsServiceImpl userDetailsService,
			BooksWsAuthneticationEntryPoint authenticationEntryPoint) {
		this.bCyptPasswordEncoder = bCyptPasswordEncoder;
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}
	
	// Override method from WebSecurityConfigurerAdapter
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		                            http.
		                                  cors().and().csrf().disable() // disable cors and csrf
										  .authorizeHttpRequests() .anyRequest()  // authorize all requests
										  .authenticated() .and().httpBasic()  // authenticate request using Basic Authentication
										  .authenticationEntryPoint(authenticationEntryPoint);  // supply authnetication entry point which we have defined in BooksWsAuthneticationEntryPoint 
		
		
	 }
	
	
	// Override method from WebSecurityConfigurerAdapter
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCyptPasswordEncoder);  // Define which userDetailsService should be used and which Password Encoder to be used
	} 


}
	


