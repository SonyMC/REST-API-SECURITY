package com.mailsonymathew.apis.booksws.book;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;


// This is the model class which we will expose to the consumers. Thsi is linked to the entity class which is hidden from the consumers.
// Note : Input Validation constraints will only work if we add dependency  'spring-boot-starter-validation' in POM
// For the validation work, we also have to use the @Valid annotation in the controller class methods where we use the Book object as teh Request body

@JsonInclude(JsonInclude.Include.NON_NULL)   // This annotation will remove hide any from the response 
public class Book {
	
	private String bookId;
	
	
	@NotNull
	@Size(min=3,max=50,message="ISBN should be atleast 3 chars and not bigger than 50 chars" )
	private String isbn;
	
	@NotNull
	@Size(min=3,max=50,message="Book title should be atleast 3 chars and not bigger than 50 chars" )
	private String title;
	
	private String publisher;
	
	private String datePublished;
	
	

	
	public Book() {

	}

	public Book(String bookId,
			    String isbn,
			    String title,
			    String publisher,
			    String datePublished )
	{

		this.bookId = bookId;
		this.isbn = isbn;
		this.title= title;
		this.publisher = publisher;
		this.datePublished = datePublished;


	}



	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(String datePublished) {
		this.datePublished = datePublished;
	}
	



}
