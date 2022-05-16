package com.mailsonymathew.apis.usersws.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRoleEntity, Integer>{  // UserRoleEntity, Primary Key
	
	
	//Here we are not trying to find the UserRoles for a given UserRole  ID or Username as the mapping will be many to one. Instead we will get the User with the related UserRoles( one-to-many mapping)
	//One to Many mapping between User and  UserRoles is defined in UserEntity.java 
	UserEntity findByUserEntity(UserEntity userEntity);

	


}
