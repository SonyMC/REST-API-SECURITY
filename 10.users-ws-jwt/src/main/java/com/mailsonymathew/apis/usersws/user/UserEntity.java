package com.mailsonymathew.apis.usersws.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/*
 * This will be used for the mapping of user object to user table in mySQL DB
 */

@Entity
@Table(name = "USERS")  // table name we have used in mySQL DB
public class UserEntity {
	
	
    @Column(name = "USERNAME") 
    @Id // username is the primary key and this can be specified by using Id annotation
     private String username;
    
    @Column(name = "PASSWORD")   
    private String password;
   
    @Column(name = "ENABLED")   
   private boolean enabled;
   
    // Many to One Mapping defined in UserRoleEntity for 'userEntity'
    // Note: Joining column is not required here as it is already defined in UserRoleEntity.java  for UserEntity . Also UserEntity is  the owner of relationship
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)  // userEntity is defined in UserRoleEntity.java
    private Set<UserRoleEntity> userRoleEntities ;  // Define a Set of UserRoleEntity as a single user can have multiple UserRoles   
    
	public UserEntity() {
	}

	public UserEntity(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<UserRoleEntity> getUserRoleEntities() {
		return userRoleEntities;
	}

	public void setUserRoleEntities(Set<UserRoleEntity> userRoleEntities) {
		this.userRoleEntities = userRoleEntities;
	}


    

}
