package com.mailsonymathew.apis.booksws.book;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path="/v1/books")
public class BookController {
	
	private static Logger logger = LoggerFactory.getLogger(BookController.class);
	
	// Get book by id : Will create a book with supplied id  
	// Sample url: http://localhost:8080/v1/books/12345
	@GetMapping(path = "/{bookId}")
	public ResponseEntity<Book> getBookbyId(@PathVariable String bookId){
		
		Book book = new Book(bookId, UUID.randomUUID().toString(), "API Security", "Chirayil Publishers", "01-01-2010");
		
		logger.info("Request to create a Book using Book ID:{}", bookId);
		
		return new ResponseEntity<>(book, HttpStatus.OK);   // Return Response entity using book object with an HTTP response code of OK.
		
	}
	
	// Post book  : will create book using Request body containing a Book
	// Sample url: http://localhost:8080/v1/books
	@PostMapping
	public ResponseEntity<Book> addBook(@RequestBody Book book){   // Send Book as a request body
		
		book.setBookId(UUID.randomUUID().toString());	  // set book id as a random UUID
		book.setIsbn(UUID.randomUUID().toString());         // set isbn as a random UUID
/*
 * Following will be supplied by in reques tbody
 */
//		book.setTitle("API Security");
//		book.setPublisher("Chirayil Publishers");
//		book.setDatePublished( "01-01-2010");
		
				
		logger.info("Request to create a Book using Book ID:{}", book.getBookId());
		
		return new ResponseEntity<>(book, HttpStatus.CREATED);   // Return Response entity using book object with an HTTP response code of CREATED
		

  } 
	
	
}	

