package com.ecom.app.service;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecom.app.dto.ProductRequest;
import com.ecom.app.dto.ProductResponse;
import com.ecom.app.entity.Product;
import com.ecom.app.repository.ProductRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository; 
	
	public ProductResponse createProduct(ProductRequest productRequest) {
		Product product = new Product();
		updateProductFromRequest(product,productRequest);
		Product saveProduct = productRepository.save(product);
		return maptoProductResponse(saveProduct);
	}


	private ProductResponse maptoProductResponse(Product saveProduct) {
		ProductResponse response = new ProductResponse();
		response.setId(saveProduct.getId());
		response.setName(saveProduct.getName());
		response.setPrice(saveProduct.getPrice());
		response.setDescription(saveProduct.getDescription());
		response.setStockQuantity(saveProduct.getStockQuantity());
		response.setCategory(saveProduct.getCategory());
		response.setImageUrl(saveProduct.getImageUrl());
		response.setActive(saveProduct.getActive());
		return response;
	}

	private void updateProductFromRequest(Product product, ProductRequest productRequest) {
		product.setName(productRequest.getName());
		product.setPrice(productRequest.getPrice());
		product.setDescription(productRequest.getDescription());
		product.setStockQuantity(productRequest.getStockQuantity());
		product.setCategory(productRequest.getCategory());
		product.setImageUrl(productRequest.getImageUrl());
	}

	/* other way to write 
	public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
		return productRepository.findById(id) .map(existingProduct -> {
			UpdateProductFromRequest(existingProduct, productRequest);
			Product saveProduct = productRepository.save(existingProduct);
			return maptoProductResponse(saveProduct);
		}).orElseThrow(()-> new RuntimeException("Product not found:" + id));
		}*/
	
	public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
		return productRepository.findById(id) .map(existingProduct -> {
			updateProductFromRequest(existingProduct, productRequest);
			Product saveProduct = productRepository.save(existingProduct);
			return maptoProductResponse(saveProduct);
		});
	}


	public List<ProductResponse> getAllProducts() {
		return productRepository.findByActiveTrue().stream().map(this::maptoProductResponse).collect(Collectors.toList());
	}

	/*this is other way but we use another way
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
		product.setActive(false);
		productRepository.save(product);
	}*/
	
	/* here product delete hard delete but not show delete in database like h2 or oracle
	public boolean  deleteProduct(Long id) {
		return productRepository.findById(id).map(product -> {
			productRepository.save(product);
			return true;
		}).orElse(false);
		
	}
	*/
	
	// that why we use this to delete soft delete
	public void deleteProduct(Long id) {
	    productRepository.findById(id).ifPresent(product -> {
	        product.setActive(false);
	        productRepository.save(product);
	    });
	}
	


	public List<ProductResponse> searchProducts(String keyword) {
		return productRepository.searchProducts(keyword).stream()
				.map(this::maptoProductResponse).collect(Collectors.toList());
	}
}
