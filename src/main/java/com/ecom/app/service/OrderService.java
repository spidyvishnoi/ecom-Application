package com.ecom.app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecom.app.dto.OrderItemDTO;
import com.ecom.app.dto.OrderResponse;
import com.ecom.app.entity.CartItem;
import com.ecom.app.entity.Order;
import com.ecom.app.entity.OrderItem;
import com.ecom.app.entity.Orderstatus;
import com.ecom.app.entity.User;
import com.ecom.app.repository.OrderRepository;
import com.ecom.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final CartService cartService;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	
	public Optional<OrderResponse>  createOrder(String userId) {
		// validate for cart items
		List<CartItem> cartItems = cartService.getCart(userId);
		if(cartItems.isEmpty()) {
			return Optional.empty();
		}
		
		
		//validate for user
		Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
		if(userOptional.isEmpty()) {
			return Optional.empty();
		}
		User user = userOptional.get();
		
		
		//Calculate total price
		BigDecimal totalPrice =  cartItems.stream()
				.map(CartItem::getPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
	
		//create order
		Order order = new Order();
		order.setUser(user);
		order.setStatus(Orderstatus.CONFIRMED);
		order.setTotalAmount(totalPrice);
		
		//here we don't have direct orderitem we hae cartitems so need to covert onto orderitem
		List<OrderItem> orderItems = cartItems.stream()
				.map(item -> new OrderItem(null, item.getProduct(),item.getQuantity(),item.getPrice(), order))
				//.collect(Collectors.toList());
				.toList();
		
		order.setItems(orderItems);
		
		Order savedOrder = orderRepository.save(order);
		
		
		//clear the cart
		cartService.clearCart(userId);
		
		return Optional.of(mapToOrderResponse(savedOrder));
	}

	private OrderResponse mapToOrderResponse(Order order) {
		
		return new OrderResponse(
				order.getId(),
				order.getTotalAmount(),
				order.getStatus(),
				
				//here we don't have direct orderitem we hae orderitemsdto so need to covert onto orderitem
				order.getItems().stream()
					.map(orderItem -> new OrderItemDTO(
						orderItem.getId(),
						orderItem.getProduct().getId(),
						orderItem.getQuantity(),
						orderItem.getPrice(),
						orderItem.getPrice()
						))
						.toList(),
						order.getCreatedAt()
						
				);
	}

}
