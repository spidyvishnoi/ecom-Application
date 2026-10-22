package com.ecom.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.ecom.app.dto.ProductRequest;
import com.ecom.app.dto.ProductResponse;
import com.ecom.app.service.ProductService;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
		return new ResponseEntity<ProductResponse>(productService.createProduct(productRequest),HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getProducts(){
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	/* other way t write when you write this code then you have to write code in product service which mention in commect section 
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest){
		return new ResponseEntity<ProductResponse>(productService.updateProduct(id, productRequest),HttpStatus.OK);
	} */
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest){
		return productService.updateProduct(id, productRequest)
				.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
	}
	
	/*this is other way but we use another
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
		productService.deleteProduct(id);
				return ResponseEntity.noContent().build();
	}*/
	
	/* here product delete hard but not show delete in database like h2 or oracle
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
		boolean deleted = productService.deleteProduct(id);
				return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}*/
	
	// thats why we use this  In your Controller
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
	    productService.deleteProduct(id);
	    return ResponseEntity.ok("Product deleted successfully");
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword){
		return ResponseEntity.ok(productService.searchProducts(keyword));
		}
}
