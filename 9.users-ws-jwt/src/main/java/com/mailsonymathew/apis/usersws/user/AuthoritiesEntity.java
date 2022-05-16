package com.mailsonymathew.apis.usersws.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * Note: We will not define 'username' ihere as it is basically defined as a foreign key to table USERS. Instead we have to defien a userentity and specify the FOreign Key using a Join Column
 */
@Entity
@Table(name = "AUTHORITIESV2") // table name we have used in mySQL DB
public class AuthoritiesEntity {

	@Column(name = "AUTHORITY_ID")
	@Id   // Primary Key
	private Integer authority_id;

	@Column(name = "AUTHORITY")
	private String authority;

	
	@Column(name = "ROLE")
	private String role;
	
	
	public AuthoritiesEntity() {

	}
	
	public AuthoritiesEntity(Integer authority_id, String authority, String role) {
		this.authority_id = authority_id;
		this.authority = authority;
		this.role = role;
	}

	// Nte:
	
	/*
	 * There exists  One to Many mapping between User and Authority as one user may have multiple authorities
	 *  Here we need to create many to one mapping between the AuthoritiesEntity and the UserEntity
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "Username", nullable = false)   //Username is the Foreign Key Used
	private UserEntity userEntity;

	 */



	public Integer getAuthority_id() {
		return authority_id;
	}

	public void setAuthority_id(Integer authority_id) {
		this.authority_id = authority_id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}







}
