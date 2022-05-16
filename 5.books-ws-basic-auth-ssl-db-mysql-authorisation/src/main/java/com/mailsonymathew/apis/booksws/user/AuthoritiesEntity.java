package com.mailsonymathew.apis.booksws.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/*
 * NOte: We wil lnot define 'username' ihere as it is basically defined as a foreign key to table USERS. Instead we have to defien a userentity and specify the FOreign Key using a Join Column
 */
@Entity
@Table(name = "AUTHORITIES") // table name we have used in mySQL DB
public class AuthoritiesEntity {

	@Column(name = "AUTHORITY_ID")
	@Id   // Primary Key
	private Integer authority_id;

	@Column(name = "AUTHORITY")
	private String authority;

	/*
	 *  One to Many mapping between User and Authority as one user may have multiple authorities
	 *  We need to create many to one mapping between the AuthoritiesEntity and the UserEntity
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "Username", nullable = false)   //Username is the Foreign Key Used
	private UserEntity userEntity;

	
	
	public AuthoritiesEntity() {

	}
	
	public AuthoritiesEntity(Integer authority_id, String authority) {
		this.authority_id = authority_id;
		this.authority = authority;
	}






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

	public UserEntity getUserentity() {
		return userEntity;
	}

	public void setUserentity(UserEntity userentity) {
		this.userEntity = userEntity;
	}
	





}
