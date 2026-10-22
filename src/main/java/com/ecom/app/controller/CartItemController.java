package com.ecom.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecom.app.dto.CartItemRequest;
import com.ecom.app.entity.CartItem;
import com.ecom.app.service.CartService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

	private final CartService cartService;
	@PostMapping
	public ResponseEntity<String>addToCart(
			@RequestHeader("X-User-ID")String userId,
			@RequestBody CartItemRequest request){
		if(!cartService.addToCart(userId, request)) {
			return ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();	 
	}
	
	@DeleteMapping("/items/{productId}")
	public  ResponseEntity<Void>removeFromCart(
			@RequestHeader("X-User-ID")String userId,
			@PathVariable Long productId){
		boolean deleted = cartService.deleteItemFromCart(userId,productId);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
	
	@GetMapping
	public ResponseEntity<List<CartItem>>getCart(
			@RequestHeader("X-User-ID") String userId){
		return ResponseEntity.ok(cartService.getCart(userId));
	}
	
}
