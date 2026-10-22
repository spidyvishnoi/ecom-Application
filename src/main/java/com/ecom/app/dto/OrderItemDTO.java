package com.ecom.app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {

	private Long id;
	private Long productId;
	private Integer quantity;
	private BigDecimal price;
	private BigDecimal subTotal;
	
}
