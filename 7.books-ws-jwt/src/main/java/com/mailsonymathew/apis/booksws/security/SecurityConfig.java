package com.mailsonymathew.apis.booksws.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private BCryptPasswordEncoder bCyptPasswordEncoder;
	private UserDetailsServiceImpl userDetailsServiceImpl;
	//private BooksWsAuthneticationEntryPoint authenticationEntryPoint;
	

	
/*
 * 	  

	public SecurityConfig(BCryptPasswordEncoder bCyptPasswordEncoder, UserDetailsServiceImpl userDetailsService,
			BooksWsAuthneticationEntryPoint authenticationEntryPoint) {
		this.bCyptPasswordEncoder = bCyptPasswordEncoder;
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}
	
 */	
	
	public SecurityConfig(BCryptPasswordEncoder bCyptPasswordEncoder, UserDetailsServiceImpl userDetailsServiceImpl) {
		this.bCyptPasswordEncoder = bCyptPasswordEncoder;
		this.userDetailsServiceImpl = userDetailsServiceImpl;
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
		                                   .authorizeRequests()  // uthorize requests using antMatcher() methods  below. Authority is also called a Role & User is also known as a Principal
										  /*
		                                   * Note : Spring Security automatically prefixes hasRole with string  'ROLE' so there is no need to specify ROLE_USER or ROLE_ADMIN explicitly as declared in DB tables.However if we explictilty prefix ROLE , Spring Security is inetlligent enough not to prefix it again 
		                                   */
		                                  .antMatchers("/v1/books/{bookId}").access("hasRole('USER') and hasAuthority('GET_BOOK')") // Refer AUTHORITIESV2 table
										  .antMatchers("/v1/books").access("hasRole('ADMIN') and hasAuthority('CREATE_BOOK')") // Refer AUTHORITIESV2 table        // authorize POST reques to create a Book only for ADMIN( the authority is defined in Authorites table for the username 'admin')
		                             	  .anyRequest() // authorize all requests
										  .authenticated() // authneticate 
		                             	  //.and().httpBasic()  // authenticate request using Basic Authentication
										  // .authenticationEntryPoint(authenticationEntryPoint);  // supply authentication entry point which we have defined in BooksWsAuthneticationEntryPoint 
		                                 .and()
		                                 .addFilter(new JwtAuthenticationFilter(authenticationManager()))  // register our JwtAuthenticationFileter 
		                                 .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsServiceImpl))  // register our JwtAuthorizationFileter 
		                              // below code disables session creation as we do not want the app to geenrate the JSESSIONID which we had seen in previous course . Hence each time only if JWT is present , will the reques tbe successfull. 	
		                                 .sessionManagement()      
		                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
	// Override method from WebSecurityConfigurerAdapter
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(bCyptPasswordEncoder);  // Define which userDetailsService should be used and which Password Encoder to be used
	    
		// E..g of Spring Secuity Using both Authorities and Roles while using inMemoryAuthentication
		//auth.inMemoryAuthentication().withUser("").password("").authorities("").roles("");  //n  number of authoritis and roles can be supplied in authorities("") androles("")
	} 


}
	


