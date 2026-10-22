package com.ecom.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.app.dto.UserRequest;
import com.ecom.app.dto.UserResponse;
import com.ecom.app.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

		//private List<User> userList = new ArrayList<>();	
		//making userService under userController

		private final UserService userService;
		
		@GetMapping
		public ResponseEntity<List<UserResponse>>getAllUsers(){
			
			return new ResponseEntity<>(userService.fetchAllUsers(),HttpStatus.OK);		//here HttpStatus.OK is just to control response entity , if take not found instead of ok then in postman its show not found response but also other side program run and get output 
			//return ResponseEntity.ok(userService.fetchAllUsers());				//this another way
		}
		
		//@RequestMapping(value = "/api/users", method = RequestMethod.GET )
		@GetMapping("/{id}")
		public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
						//return userService.fetchUser(id);								//way 1 without ResponseEntity
						//return ResponseEntity.ok(userService.fetchUser(id));			//way2 with ResponseEntity but only ok msg
						/*
						User user = userService.fetchUser(id);
						if(user==null) 
							return ResponseEntity.notFound().build();
							return ResponseEntity.ok(user);
					}*/
			//Modern java 
			return userService.fetchUser(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
		}	
		
		@PostMapping
		public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest){
			userService.addUser(userRequest);
			return ResponseEntity.ok("User Added successfully");
		}
		
		
		//designing endpoint
		@PutMapping("/{id}")			//here {id} is path variable
		public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest updateUserRequest){			//springboot doing greate job here just adding this anitotion @RequestBody which sending json and auotomatically convereted under user object type  
			
			boolean updated = userService.updateUser(id, updateUserRequest);
			if(updated) {
				return ResponseEntity.ok("User Updated successfully");
			}
					return ResponseEntity.notFound().build();
		}
		
		

}
