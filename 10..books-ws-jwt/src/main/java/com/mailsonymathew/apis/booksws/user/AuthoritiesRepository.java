package com.mailsonymathew.apis.booksws.user;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<AuthoritiesEntity, Integer>{  // AuthoritiesEntity, Primary Key
	
	
	//Here we are not trying to find the Authorities for a given Authority ID or Role as the mapping will be many to one. Instead we will get a Set of Authorities using the input role
	//One to Many mapping between Roles and  Authority is defined in RolesEntity.java 
	Set<AuthoritiesEntity> findByRole(String role);

	


}
