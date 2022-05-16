package com.mailsonymathew.apis.booksws.user;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserRepository {
	
	private static List<User> users;  // create static list of users 
	
	// We need Object Mapper to read an dmap the user-credentials.json  file 
	private static ObjectMapper mapper  = new ObjectMapper(); 
	
	// Load the the user-credentials.json  file statically  once ( so we will not require to load it each time )
	static {
		try {
			File file = ResourceUtils.getFile("classpath:user-credentials.json" );  // Load user-credentials.json file from resources folder
			// map the values in the file . We use TypeReference as the users is provided as a list in the user-credentials.json file
			users = mapper.readValue(file, new TypeReference<List<User>>() {   // Leae implmentation empty
			});
		} catch(IOException ex) {
			ex.printStackTrace();  // Normally the exception should be logged
			}
	}
	
	 // Fetch the user ( Optioanl to cater for User not breign found) 
	public static Optional<User> findByUserName(String username) {
		
		return users.stream()   // stream to use filter operation to find our user from the list id users loaded from the user-credentials.json file
				 .filter(user -> user.getUsername().equals(username) )
				 .findFirst();
		
	}
	

	
}	