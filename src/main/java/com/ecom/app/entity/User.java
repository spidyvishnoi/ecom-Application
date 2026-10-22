package com.ecom.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;



@Data
@Entity(name = "user_table")
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		//this generate unique id
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private UserRole role= UserRole.CUSTOMER;
	
	/* //Default constructor
	public User()
		
	}
	
	
	//constructor
	public User(Long id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName
	}*/ 
	//instead of using  these constructor which  we do manually . we can use @NoArgsConstructor & @AllArgsConstructor 
	
	//join
		@OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
		@JoinColumn(name = "address_id", referencedColumnName = "id")
		private Address address;
		
		@CreationTimestamp
		private LocalDateTime createdAt;
		
		@UpdateTimestamp
		private LocalDateTime updateAt;
}
