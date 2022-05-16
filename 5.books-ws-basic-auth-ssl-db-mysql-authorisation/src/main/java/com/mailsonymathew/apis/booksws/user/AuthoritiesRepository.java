package com.mailsonymathew.apis.booksws.user;

import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<AuthoritiesEntity, Integer>{  // AuthoritiesEntity, Primary Key
	
	
	//Here we are not trying to find the Authorities for a gven Authority ID or Username as teh mapping will be many to one. Instead we will get the User with th related authorities( one-to-many mapping)
	//One to Many mapping between User and  Authority is defined in UserEntity.javaby decalration of 'private Set<AuthoritiesEntity> authorityEntities'
	UserEntity findByUserEntity(UserEntity userEntity);

	


}
