package com.mailsonymathew.apis.usersws.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController {
	
	@RequestMapping("/login")   // url: https://localhost:8082/users/login
	public String loginUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
		
		if(oAuth2User != null) {
			
			return "UserName: " + oAuth2User.getName() + "<br>"
					+ "UserAuthorities: " + oAuth2User.getAuthorities();
		}else {
			return null;
		}
		
		
	}

}
