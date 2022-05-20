package com.mailsonymathew.apis.booksws.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service  // Will add this class as a bean to application context so that it is auto-discovered
public class UserDetailsServiceImpl  implements UserDetailsService{


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			return new User();
		


	}

}
