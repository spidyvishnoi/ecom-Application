package com.ecom.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ecom.app.entity.Orderstatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {

	private Long id;
	private BigDecimal totalAmount;
	private Orderstatus status;
	private List<OrderItemDTO> items;
	private LocalDateTime createdAt;
}
