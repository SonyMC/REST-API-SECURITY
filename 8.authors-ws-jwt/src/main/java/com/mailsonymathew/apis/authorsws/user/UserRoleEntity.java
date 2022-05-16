package com.mailsonymathew.apis.authorsws.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/*
 * Note: 
 *  - We will not define 'username' here as it is basically defined as a foreign key to table USERS. Instead we have to define a userentity and specify the Foreign Key using a Join Column
 * -  We will not define
 */
@Entity
@Table(name = "USER_ROLE") // table name we have used in mySQL DB
public class UserRoleEntity {

	@Column(name = "USER_ROLE_ID")
	@Id   // Primary Key
	private Integer userRoleId;

	@Column(name = "ROLE")
	private String role;

	
	public UserRoleEntity() {

	}


	public UserRoleEntity(Integer userRoleId, String role) {
		super();
		this.userRoleId = userRoleId;
		this.role = role;
	}
	
	/*
	 * There exists  One to Many mapping between User and User Roles as one user may have multiple roles
	 *  Here we need to create many to one mapping between the UserRoleEntity and the UserEntity
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "Username", nullable = false)   //Username is the Foreign Key Used
	private UserEntity userEntity;


	public Integer getUserRoleId() {
		return userRoleId;
	}


	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public UserEntity getUserEntity() {
		return userEntity;
	}


	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}



   





}
