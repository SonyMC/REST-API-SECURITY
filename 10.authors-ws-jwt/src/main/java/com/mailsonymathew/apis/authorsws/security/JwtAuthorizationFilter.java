package com.mailsonymathew.apis.authorsws.security;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;



//1. This class will be called by the user after getting the JWT token in the JwtAuthenticationFilter class in User-ws.
//2. This class will essentially allow the user to call subsequent APIs using the JWT issued in JwtAuthenticationFilter class.
//3. We will extend BasicAuthenticationFilter which is used to authorize details in the Http Request Header 
public class JwtAuthorizationFilter  extends BasicAuthenticationFilter { // Note: BasicAuthenticationFilter will be used  in Spring Security Filter chain when the JWT token is passed in the Authorization Header

	// create logger
	private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
	
	// Define the jwt signing secret as an environment variable 
	private static String envSigningSecret = System.getenv("envSigningSecret");
	

	
   public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
	        super(authenticationManager);
	        
	    }



	// Get authorization header and do subsequent processing.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		// Extract the JWT token from the Request Header  . Teh JWT was generated  earlier in the Response header through the login request  via JwtAuthenticationFilter class.
		String authorizationHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER); // AUTHORIZATION_HEADER = '"Authorization"
		
		// Validate the Authorization Header in Request containing the JWT
		// In case there is no authorisation header OR the authorisation header length is zero OR there is one but it does not start with  "Bearer " 
		// then ignore header & return control back to filter back to filter chain 
		if( authorizationHeader == null	||    
			authorizationHeader.trim().length() == 0 ||	
			!authorizationHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)){  //BEARER_TOKEN_PREFIX = "Bearer "
			chain.doFilter(request, response);   // progress the chain 
			return;
		}
		
		// if everything is OK,then :
		 //1. Authenticate the JWT token available in the Authorization Header by decrypting the jwt using the same secret key that we used fro jwt creation
		// 2. Set the Security context using the Authenticated token
		
		UsernamePasswordAuthenticationToken authenticatedToken= getAuthentication(authorizationHeader);  //getAuthentication() is defined below
						
		// If everything is OK then set the token in the Security Context Holder
		SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
		
		// finally continue with the filter chain 
		chain.doFilter(request, response);
		
	}


	private UsernamePasswordAuthenticationToken getAuthentication(String authorizationHeader) {
		
		// the same secret key used in JwtAuthenticationFilter class will be required to decrypt the token 
		//Algorithm algorithmHS = Algorithm.HMAC512(SecurityConstants.SIGNING_SECRET.getBytes());
		Algorithm algorithmHS = Algorithm.HMAC512(envSigningSecret);   // use the signing secret from system environment which is more safe!!!

		if(authorizationHeader != null){
			
		
			
			try{
				
		   DecodedJWT decodedJWT = 
				   JWT  // decrypt the JWT token in header using the secret key 
					.require(algorithmHS)   // use the same secret key to decrypt for verification
					.build() //JWT verifier
					.verify(authorizationHeader.replace(SecurityConstants.BEARER_TOKEN_PREFIX, "")) ; // remove the prefix we had put in previously 
		   
		  
			if(decodedJWT != null) { 
				
				// Get Subject
				String userNameFromJwt =  decodedJWT.getSubject(); 
					

			
			// Verify username and return Authenticated Token containing username and authorities
			if ((userNameFromJwt != null) &&  
				((userNameFromJwt.equalsIgnoreCase("user1")) || (userNameFromJwt.equalsIgnoreCase("admin")) ))

					{
				
				// Get Claims 
				Set<SimpleGrantedAuthority> authorities = 
										Stream.of(decodedJWT.getClaim("Roles")  // Authorities are stored under claim named 'Roles' ( refer JwtAuthenticationFilter.java in user-ws) and separated using commas 
										.asString().split(","))   
										.map(authority -> new SimpleGrantedAuthority(authority))
										.collect(Collectors.toSet());
				
				// Input parms are Principal, Credentials and Authority Collection. We will pass the credentials or pwd as empty.l
				return new UsernamePasswordAuthenticationToken(userNameFromJwt, null, authorities);  
				/*
				 * Note : If we are not setting authorities we can pass an empty collection as follows
				 */
				// return new UsernamePasswordAuthenticationToken(userNameFromJwt, null, new ArrayList<>());

					}
				}
			}catch(BadCredentialsException bex){
				logger.error("Authorization Failed!!",bex.getMessage());
				throw bex;
			}catch(Error ex){
				logger.error("Error while authorizing!!!",ex.getMessage());
				throw new RuntimeErrorException(ex);
			}

		}
		return null;
		
	}

}
