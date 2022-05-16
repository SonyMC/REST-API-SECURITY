package com.mailsonymathew.apis.booksws.security;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailsonymathew.apis.booksws.user.User;

//import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

//1.This class is first in chain of authorisation responsible for authenticating the input usser & pwd and returnign a jwt token
//2. In this case the username and pwd will need to be set using a login REST API as we do not have web form UI
//3. Spring automatically figures where this filter should fit in the whole security chain as we are extending 'UsernamePasswordAuthenticationFilter( though if we want we can define the trigerring point)..
//4. Once this class is executed, the user will take the JWT token returned and call the JWTAuthorizationFilter class which will be the second in command to this class. 
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter { // Note :
																					// UsernamePasswordAuthenticationFilter
																					// is used in Spring Security Filter
																					// chhian when Username and Password
																					// is passed in request body

	// create logger
	private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	// Note:We cannot define contant values in application.properties file for hhis calss as follows as then we will have to annotate this calls with @Componentor @Service or @Repository to extract usign @Value.
	// Retrieve constants specified in application.properties file
	// @Value("${bookws.security.jwt.token.jwt-id}")
	// private String jwt_id;

	// Authentication Manager does the authentication using whatever functionality
	// we define.
	private AuthenticationManager authenticationManager;
	
	// Define the jwt signing secret as an environment variable 
	private static String envSigningSecret = System.getenv("envSigningSecret");

	// constructor based dependency injection
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	// 1, Authenticate Username and Pasword and generate Authnetication Token ( wich
	// will be passed to successfulAuthentication() function below )
	// Authenticate the input user name & password in the request
	// Login request: http://localhost:8080/login . Provide username and pwd as json
	// body in request.
	// The authenticate() function validates the user name and pwd stored in db
	// which is retrieved by the UserDetailsServiceImpl class.

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		User inputUser = null;

		Authentication authenticate = null;

		try {
			// Construct User object using Jackson mapper on the input request with
			// reference to User class
			inputUser = new ObjectMapper().readValue(request.getInputStream(), User.class); // username and password
																							// declaration can be found
																							// in User.java
			// Now generate authentication token for User name & pwd
			// Once the authenticate() function is called successfully , the Authenticate
			// instance will contain the details of the user as loaded from the DB.
			authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					inputUser.getUsername(), inputUser.getPassword(), new ArrayList<>())); // ArrayList<> in input will
																							// usually contain
																							// authorities . However we
																							// do not require this here
																							// as we are going to
																							// authenticate

		} catch (BadCredentialsException badcrex) {
			// Authentication Has failed if control reaches this point
			logger.error("Authentication Failed for UserName:{}!!!", inputUser.getUsername(), badcrex.getMessage());
			throw badcrex;
		} catch (Error ex) {
			logger.error("Error while authentication the userName:{}!!!", inputUser.getUsername(), ex.getMessage());
			throw new RuntimeErrorException(ex);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authenticate; // authentication token

	}

	// 2. Generate JWT as a Response Header from the Authentication token generated
	// above:
	// Now on successful authentication of username and password above , generate
	// the JWT token from authenmtication token geenrated above
	// the authentication chain is automatically redirected to this function from
	// the above attemptAuthentication funnction
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		// Get user from authentication token.
		// Here the principal object in the authentication token is the user as loaded
		// from the DB in the above function
		User authUser = (User) auth.getPrincipal(); // Spring Security will have placed the principal in the cottext
													// after authentication

		// hash the secret key using HMAC512 algorithm
		//Algorithm algorithmHS = Algorithm.HMAC512(SecurityConstants.SIGNING_SECRET.getBytes());
		Algorithm algorithmHS = Algorithm.HMAC512(envSigningSecret);   // use the signing secret from system environment which is more safe!!!

		// Now generate JWT token
		String jwtToken = JWT
				.create()
				.withSubject(authUser.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.withClaim("Username", authUser.getUsername())
				.withClaim("Enabled", authUser.isEnabled())
				.withKeyId(SecurityConstants.Key_ID)
				.withJWTId(SecurityConstants.JWT_ID).sign(algorithmHS);

		/*
		 * Now attach this JWT token to the http response header
		 */
		// response.addHeader("Authorization", "Bearer " + jwtToken);
		response.addHeader(SecurityConstants.AUTHORIZATION_HEADER, SecurityConstants.BEARER_TOKEN_PREFIX + jwtToken);

	}
   
	
	/*
	 *  Note: Sample code - The below Function is provided as a sample to demonstrate on the geenrated jwt can be extracted and passed back as a string
	 *  We will not be usinfg this function in this particular applicaiton as we will manually get the response header cotaining the jwt and use it for subesequent requests via tehrequest authorization header!!!
	 */
//	protected String extractJwtTokenFromResonseHeader(HttpServletRequest request, HttpServletResponse response) throws JWTVerificationException {
//
//		try {
//			String jwt = response.getHeader(SecurityConstants.AUTHORIZATION_HEADER); // AUTHORIZATION_HEADER =	"Authorization"
//
//			// Validate we havae the correct JWT token:
//			if (jwt != null && 
//				jwt.trim().length() != 0 && 
//				jwt.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) { // BEARER_TOKEN_PREFIX = "Bearer"
//				return jwt;
//			}
//
//		} catch (JWTVerificationException jwtEx) {
//			logger.error("JWT extraction failed!!", jwtEx.getMessage());
//			throw jwtEx;
//		}
//		return null;
//
//	}

}
