package com.mailsonymathew.apis.booksws.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


// Class to fetch user details 
/* Password : 
*   https://bcrypt-generator.com/ : Use this  link to geenrate hash of string 'myuserpassword'	
*/
@Service
public class UserService {
	
	// Define slf4j logger 
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	 
	public UserService() {
		
		
	}
	
			
// Method will be used in UserDetailsServiceImpl.java
	public User getUserByUserName(String username) {
		var userOptional = UserRepository.findByUserName(username);    // Extract UserOptional from UserRepository . Note 'findByUserName' is a static method and hence we do not need to instantiate UserRepository 
		if(userOptional.isPresent()) {
			return userOptional.get() ;  // get User from Optioanl
		}else{
			return null;
		}
	}
	 		

}
