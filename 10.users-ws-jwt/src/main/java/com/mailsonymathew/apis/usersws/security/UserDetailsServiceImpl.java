package com.mailsonymathew.apis.usersws.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mailsonymathew.apis.usersws.user.User;
import com.mailsonymathew.apis.usersws.user.UserService;

@Service  // Will add this class as a bean to application context so that it is auto-discovered
public class UserDetailsServiceImpl  implements UserDetailsService{

	// Note: User Details will be supplied by UserService.java
	private UserService userService;

	
	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByUserName(username);
		if(user != null) {
			return user;
		}else {
			throw new UsernameNotFoundException(username + " does not exist!!!");
		}
		


	}

}
