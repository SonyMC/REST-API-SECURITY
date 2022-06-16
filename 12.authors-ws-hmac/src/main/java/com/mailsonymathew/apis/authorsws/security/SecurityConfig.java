package com.mailsonymathew.apis.authorsws.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;




@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	

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
		                                   .authorizeRequests()  // authorize requests using antMatcher() methods  below. Authority is also called a Role & User is also known as a Principal
			                                 // .antMatchers("/v1/authors/{authorId}").access("hasRole('USER') and hasAuthority('GET_AUTHOR')") 
											 // .antMatchers("/v1/authors").access("hasRole('ADMIN') and hasAuthority('CREATE_AUTHOR')")  
		                                 //   .antMatchers("/v1/authors/{authorId}").access("hasAuthority('Dev_USER') and hasAuthority('Dev_GET_AUTHOR')") // Refer Okta -> Directory-> Groups 
								         //.antMatchers("/v1/authors").access("hasAuthority('Dev_ADMIN') and hasAuthority('Dev_CREATE_AUTHOR')") // Refer Okta -> Directory-> Groups 
										  .anyRequest() // authorize all requests
										  .authenticated() // authentication via Ok	ta
		                             	  //.and().httpBasic()  // authenticate request using Basic Authentication
										  // .authenticationEntryPoint(authenticationEntryPoint);  // supply authentication entry point which we have defined in BooksWsAuthneticationEntryPoint 
		                                 .and()
		                             //    .addFilter(new JwtAuthenticationFilter(authenticationManager()))  // register our JwtAuthenticationFileter  -> This has been moved to users-ws.java service
		                             //   .addFilter(new JwtAuthorizationFilter(authenticationManager()))  // register our JwtAuthorizationFileter 
		                               .addFilter(new HmacFilter(authenticationManager())) ; // register our HMAC Authentication Filter   
		                                 // below code disables session creation as we do not want the app to generate the JSESSIONID which we had seen in previous course . Hence each time only if JWT is present , will the reques tbe successfull. 	
		                             //    .sessionManagement()      
		                            //    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		                            //    .oauth2Login();    // SpringBoot will automatically add dependencies, configure beans and load then in the application context 
	}
	
	
	/*
	 * Note: below configuration is not required for OAuth2.0	
	 */
	
//	// Override method from WebSecurityConfigurerAdapter
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	    auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(bCyptPasswordEncoder);   
//		
//	    // E..g of Spring Security Using both Authorities and Roles while using inMemoryAuthentication
//		//auth.inMemoryAuthentication().withUser("").password("").authorities("").roles("");  //n  number of authorities and roles can be supplied in authorities("") androles("")
//	} 


}
	


