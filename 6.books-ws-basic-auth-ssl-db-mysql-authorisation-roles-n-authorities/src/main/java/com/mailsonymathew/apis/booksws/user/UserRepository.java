package com.mailsonymathew.apis.booksws.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


//@Service
@Repository  // We have to use the REpository annotation for a DB
public interface  UserRepository extends CrudRepository<UserEntity, String> {
	
	UserEntity findByUsername(String username);    

	

	
}	