package com.mailsonymathew.apis.authorsws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/*
 * Note: Spring does not provide any standard HMAC filter. Hence we have to create our own customized filter using Spring's BasicAuthenticationFilter 
 */
public class HmacFilter extends BasicAuthenticationFilter{

	// create logger
	private static Logger logger = LoggerFactory.getLogger(HmacFilter.class);
	
	
	// Define HashMap for  User Credentials
    private static Map<String, String> hmacUserCredentials = new HashMap<> ();
    /*
     * Note : The below credentials are hard-coded and is not recommended. Instead store in a DB in encrypted format. Refer  project :"5.books-ws-basic-auth-ssl-db-mysql-authorisation"
     */
    // Load User credentials in the Hash Map 
        static {
        hmacUserCredentials.put("partner-1", "partner-1-secret");
        hmacUserCredentials.put("partner-2", "partner-2-secret");
        hmacUserCredentials.put("partner-3", "partner-3-secret");

    }
	
	public HmacFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	
	// Get Headers and do subsequent processing.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		// Fetch  Header("X-HMAC-Value") from Request
		String hmacHeader = request.getHeader("X-HMAC-Value");  //Get Header with name X-HMAC-Value provided in the request
		
		//Validate  hmac Header  ( Note: Client will send this in Request Header)
		if(hmacHeader == null || hmacHeader.equals("")) {
			chain.doFilter(request, response);  // return control back to filter chain
			logger.info("HMAC Header is empty!!!");
			return;
		}
		
		
		// Fetch  Header("X-Username") from Request ( Note: Client will send this in Request Header)
		String username = request.getHeader("X-Username");
		
		
	
		//Validate  username Header 
        if((username != null || !username.equals("")) && (hmacUserCredentials.containsKey(username)))  {  // username cannot be null or spaces & must be availab ale in the hashmap defined in the beginning of thsi class
        	String secret = hmacUserCredentials.get(username);    // extract the secret value for the user from the static hashmap declared in the beginning of this code
           	String httpVerb = request.getMethod();      // extract the method used . Note: There is no need for client to send this and will be available in htpp requests
            String path = request.getServletPath();   // extract the path  .Note: There is no need for client to send this and will be available in htpp requests
           // Fetch Header("X-Date") from Request
            String dateHeader = request.getHeader("X-Date");     
            // Calculate B64 encoded  Hmac Value using the verb, path , dateHeader and secret
            String base64OfHmacValue = calculateBase64andHmacValue(httpVerb, path, dateHeader, secret);
            
            // Declare Spring Security Authentication Token
            UsernamePasswordAuthenticationToken authenticationToken = null;
            
            // If the B64 encoded HMAC value matches the value value sent by client in the "X-HMAC-Value" Header, then create a new Authentication token using Spring Securoty
            if(base64OfHmacValue != null && base64OfHmacValue.equals(hmacHeader)) {
                authenticationToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());  // refer  original method to understand implmentation
                logger.info("Authentication Token Created!!!");
            }
            
            // Set the authentication token in the Spring Security Context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // Finally Resume the chain with the Authentication Filter
            chain.doFilter(request, response);
            
		 }else {
			 return;  // if username is null. no processing and continue the chain 
		 }
	

	}
	
	
	// Function to calculate Hmac value and do a B64 using 4 parameters extracted in the function above (the Http verb, path, dateHeader and secret)
    private String calculateBase64andHmacValue(String httpVerb, String path, String dateHeader, String secret) {
     
    	// Validate all 4 parms
        if(httpVerb == null || path == null || dateHeader == null || secret == null) {
            return null;
        }

        // Create the string to sign using the httpVerb, path and date header
        String stringToSign = httpVerb + "::" + path + "::" + dateHeader;
        // Use Apache library  commons-codec  to calculate Hmac( Hash Method Authentication Code) using the secret
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret);
        // Now Hash the string to sign with the HMAC  of the secret 
        String hmacHex = hmacUtils.hmacHex(stringToSign);
        // Finally B64 encode the Hash
        return Base64.getEncoder().encodeToString(hmacHex.getBytes());
    }
}	
