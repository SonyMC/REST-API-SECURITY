package com.mailsonymathew.apis.booksws.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


// Class to fetch user details 
@Service
public class UserService {
	
	// Define slf4j logger 
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	 
	// User object
	private User user = null;

   // User will be created in memory as soon as application starts	
   /* Password : 
    *   https://bcrypt-generator.com/ : Use this  link to geenrate hash of string 'myuserpassword'	
   */
	public UserService() {
		user = new User("myusername","$2a$12$A/KX9V2n5c4Nf1AMUw/BN.o2U.QR6H1cvRge51EZourVylnPJfBM." , true);
	}
	
	// Method will be used in UserDetailsServiceImpl.java
	public User getUserByUserName(String username) {
		if(user.getUsername().equals(username)) {
			return user;
		}else{
			return null;
		}
	}
			

	 		

}
