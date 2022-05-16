package com.mailsonymathew.apis.booksws.user;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		
		// 1. Create User from UserEntity
		User user = new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.isEnabled());
		                     
		
		//2. Fetch Authorites from Authorities table
		// Note: 
		//      - Flow wil be UserEntity -> UserRoleEntity -> RoleEntity -> AuthoritiesEntity
		//     -  Flow will make better sene if referred to the Entity Relationship Diagram in Slide 10 of ppt for this course
		Stream<Stream<String>> streamStreamAuthorities = 
		userEntity  // Start with User Entity which passed as input parm to this method
			.getUserRoleEntities()  // Get all User Role Entities. Returns Set<UserRoleEntity>
			.stream() // Stream each UserRoleEntity
			.map(userRoleEntity -> {
				Set<AuthoritiesEntity> authorityEntities = authoritiesRepository.findByRole(userRoleEntity.getRole());// Using the Role from the UserRoleEntity get the Authorities Set
				// Now get each Authority from the Set<AuthoritiesEntity> and return the Stream
				return authorityEntities
				      .stream()  // stream the Set of AuthorityEntities 
				      .map(authorityEntity ->authorityEntity.getAuthority());  // get individual authoritiy 
			});
				
		//3. Now Flatten the Stream of Stream to get the Set of Authorities
		Set<String> authorities = streamStreamAuthorities
			.flatMap(authorityStream -> authorityStream)
		    .collect(Collectors.toSet());
		
		// 4. Now add the Role from the User_Role table as Authorities because SpringSecurity UserDetails does not support adding Roles Seprately as it does not have a SetRole method
		//	- 		
		// Note: 
		//     - 	The Spring Security class SimpleGrantedAuthority implements GrantedAuthority has a just a definition of String Role and it has separate method to set the Role  
		//     -   In User.java we are only setting the Authorities and there is no function to set roles
		//     - In short, we need to have roles and authorites need to be extracted together from authorites
		
		userEntity
			.getUserRoleEntities()   // Returns Set<UserRoleEntity>. Get all UserRoles from the UserEntity ( because UserEntity and UserRoleEntity are joined via a FK relationship using Username column 
			.stream()    // Stream the Set<UserRoleEntity>
			.forEach(userRoleEntity -> authorities.add(userRoleEntity.getRole()));   // For each userRoleEntity , add it it to the AuthoritiesEntity( thsi is possible beucae UserRoles and AAuthorities both have a one-to-many relationship with the Roles tables, 
		
		
		// 5. Now set the authorites for the user 
		user
			.setAuthorities
			(
			authorities   
			.stream()   // Stream the Set of Authorities 
			.map(auth -> new SimpleGrantedAuthority(auth))  // Map each authority to a SimpleGrantedAuthority
			.collect(Collectors.toSet())  // Set of SimpleGrantedAuthority
			);
		
		logger.info("Create a user:{} = ", user);
		
		
		//6. Finally return the Uer with Authorites and Roles
		return user;

	}

}
