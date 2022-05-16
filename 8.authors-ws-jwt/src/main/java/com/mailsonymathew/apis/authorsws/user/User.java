package com.mailsonymathew.apis.authorsws.user;

import java.util.Collection;
import java.util.Set;

import javax.validation.constraints.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)   // This annotation will remove/hide any  non-null from the response 
public class User  implements UserDetails{  // We are integrating Spring Security UserDetails with our User Model 

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Size(min = 1, max = 50, message
            = "Username must be between 1 and 50 characters")
    private String username;

    @Size(min = 8, max = 20, message
            = "Password must be between 8 and 20 characters")
    private String password;

   private boolean enabled;
   
   // Authorization: Declare Set containing Granted Authorities
   private Set<GrantedAuthority> authorities;

    public User() {
    }

	public User(@Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters") String username,
			@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters") String password,
			boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	
	/*
	 * Thsi method will be used for granting required authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return enabled;   // we have defined this boolean variable
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return enabled;
	}


    
    

}
	