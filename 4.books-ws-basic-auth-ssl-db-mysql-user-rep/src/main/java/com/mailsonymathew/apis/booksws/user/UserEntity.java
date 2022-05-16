package com.mailsonymathew.apis.booksws.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/*
 * This will be used for teh mapping of user obeject to user table in mySQL DB
 */

@Entity
@Table(name = "USERS")  // table name we have used in mySQL DB
public class UserEntity {
	
//    @Column(name = "User_Id")
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userId_generator")
//    @SequenceGenerator(name="userId_generator", sequenceName = "user_sequence", allocationSize=1)
//    private int userId;

	
    @Column(name = "USERNAME") 
    @Id // username is the primary key and thsi can be specified by using Id annotation
     private String username;
    
    @Column(name = "PASSWORD")   
    private String password;
   
    @Column(name = "ENABLED")   
   private boolean enabled;
    
    

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
	
    

}
