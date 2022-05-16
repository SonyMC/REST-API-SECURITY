package com.mailsonymathew.apis.booksws.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/*
 * Note: Will be called during basic authentication error
 */
@Component // auto-discover
public class BooksWsAuthneticationEntryPoint extends BasicAuthenticationEntryPoint {  // extend Spring Security BasicAuthenticationEntryPoint
	


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {   // accepts Http request, Http Response and Authentication Exception
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName());  // add Header ( name =WWW-Authenticate) with Basic Realm teh value of  which is set using the  afterPropertiesSet() below
		
		//response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()); if we use sendError it wil loverwrite the PrintWriter response
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED) ;     
		// Use PrintWriter and send error message in Http Body 
		PrintWriter writer= response.getWriter();
		writer.println("Basic Authentication Required. Please supply appropriate credentials!!!");
	}

	// Set the Basic realm
	@Override
	public void afterPropertiesSet() {
		setRealmName("Something Missing Buddy!!!");  // Set the Basic Realm value
		super.afterPropertiesSet();
	}  
}
