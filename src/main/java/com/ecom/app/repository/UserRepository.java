package com.ecom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository; also you can use this
import org.springframework.stereotype.Repository;

import com.ecom.app.entity.User; 

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
