package com.mailsonymathew.apis.booksws.user;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

// Class to fetch user details 
/* Password : 
 *   https://bcrypt-generator.com/ 
*/

@Service
public class UserService {

	// Define slf4j logger
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	private UserRepository userRepository;

	private AuthoritiesRepository authoritiesRepository;

	public UserService(UserRepository userRepository, AuthoritiesRepository authoritiesRepository) {
		this.userRepository = userRepository;
		this.authoritiesRepository = authoritiesRepository;
	}

// Method will be used in security/UserDetailsServiceImpl.java
	public User getUserByUserName(String username) {
		var userEntity = userRepository.findByUsername(username);
		if (userEntity != null) {
			return createUserFromUserEntity(userEntity);
		} else {
			return null;
		}
	}

	// Create User from User Entity
	private User createUserFromUserEntity(UserEntity userEntity) {
		User user = new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.isEnabled());
		// Set the Authorities
		user.setAuthorities(
				                      userEntity
				                      .getAuthorityEntities()  // returns Set<AuthoritiesEntity>
				                      .stream()   // Stream each Authorities Entity
				                      .map(authoritiesEntity -> new SimpleGrantedAuthority(authoritiesEntity.getAuthority()))  // map to a SimpleGrantedAuthority
				                      .collect(Collectors.toSet())  // Return a set of Granted Authorities 
				                      );
				                      
		logger.info("Request to create a user:{} = ", user);
		return user;

	}

}
