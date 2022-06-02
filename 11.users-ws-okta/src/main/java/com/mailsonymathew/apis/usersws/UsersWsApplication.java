package com.mailsonymathew.apis.usersws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class UsersWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersWsApplication.class, args);
	}

//    /*
//     * If we do not declare the BCryptPasswordEncoder bean , application will fail to start:
//     */
//    	
//       @Bean
//      
//       public BCryptPasswordEncoder bCryptPasswordEncoder() {
//    	   return new BCryptPasswordEncoder();
//       }

}
