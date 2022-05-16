package com.mailsonymathew.apis.booksws.security;

import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mailsonymathew.apis.booksws.user.AuthoritiesEntity;

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
									/*
									 * CSRF (Cross-Site Request Forgery ) is an attack that forces authenticated users to submit a request to a Web application against which they are currently authenticated. CSRF attacks exploit the trust a Web application has in an authenticated user.
									 * CORS (
Cross-Origin Resource Sharing )is a mechanism to allow two different domains to talk to each other (by relaxing same-origin policy)
									 */
		                            http.
		                                  cors().and().csrf().disable() // disable cors and csrf : 
										  .authorizeHttpRequests() // authorize requests using antMatcher() methods  below. Authority is also called a Role & User is also known as a Principal
										  .antMatchers("/v1/books/{bookId}").hasAuthority("USER")   // authorize GET request by book id only if user has an authority of USER ( we have added Set<AuthoritiesEntity> while to USER creation in UserService.java(( the authority is defined in Authorites table for the username 'user1'))
										 // .antMatchers("/v1/books/{bookId}").hasAnyAuthority("USER","ADMIN") // authorize GET request fro both USER and ADMIn authorities
										  .antMatchers("/v1/books").hasAuthority("ADMIN")        // authorize POST reques to create a Book only for ADMIN( the authority is defined in Authorites table for the username 'admin')
										  .anyRequest()  // authorize all requests
										  .authenticated() .and().httpBasic()  // authenticate request using Basic Authentication
										  .authenticationEntryPoint(authenticationEntryPoint);  // supply authentication entry point which we have defined in BooksWsAuthneticationEntryPoint 
		
		
	 }
	
	
	// Override method from WebSecurityConfigurerAdapter
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCyptPasswordEncoder);  // Define which userDetailsService should be used and which Password Encoder to be used
	} 


}
	


