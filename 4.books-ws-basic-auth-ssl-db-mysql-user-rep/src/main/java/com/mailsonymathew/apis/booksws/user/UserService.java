package com.mailsonymathew.apis.booksws.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


// Class to fetch user details 
/* Password : 
 *   https://bcrypt-generator.com/ 
*/

@Service
public class UserService {
	
	// Define slf4j logger 
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
@Autowired   // Will automatically create and inject the dependency 	and hence there is no need to specifically delcare a constructor
   private UserRepository userRepository;

//   
//   public UserService(UserRepository userRepository) {
//	this.userRepository = userRepository;
//}

			
// Method will be used in UserDetailsServiceImpl.java
	public User getUserByUserName(String username) {
                    var userEntity = userRepository.findByUsername(username);
                    if(userEntity != null) {
                    	return createUserFromUserEntity(userEntity);
                    }else {
                    	return null;
		}
	}


    // Create User from User Entity
    private User createUserFromUserEntity(UserEntity userEntity) {
    	User user = new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.isEnabled());
    	logger.info("Request to create a user:{} = ", user);
    	return user;

    }
	 		

}
