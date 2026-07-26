package com.cdac.onlineTiffinService.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.onlineTiffinService.dto.OrderResponseDto;
import com.cdac.onlineTiffinService.dto.PlaceOrderRequestDto;
import com.cdac.onlineTiffinService.model.Status;
import com.cdac.onlineTiffinService.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
	private final OrderService orderService;
	
	
	@PostMapping("/customer/{customerId}")
    public ResponseEntity<OrderResponseDto> placeOrder(
            @PathVariable Long customerId,
            @Valid @RequestBody PlaceOrderRequestDto request) {

        OrderResponseDto response =
                orderService.placeOrder(customerId, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDto> getOrderById(
	        @PathVariable Long orderId) {

	    return ResponseEntity.ok(orderService.getOrderById(orderId));
	}
	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<OrderResponseDto>> getMyOrders(
	        @PathVariable Long customerId) {

	    return ResponseEntity.ok(
	            orderService.getMyOrders(customerId));
	}
	
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponseDto> cancelOrder(
	        @PathVariable Long orderId) {

	    return ResponseEntity.ok(
	            orderService.cancelOrder(orderId));
	}
	
	@GetMapping("/customer/{customerId}/status/{status}")
	public ResponseEntity<List<OrderResponseDto>> getMyOrdersByStatus(
	        @PathVariable Long customerId,
	        @PathVariable Status status) {
		

	    return ResponseEntity.ok(
	            orderService.getMyOrdersByStatus(customerId, status));
	}
	@PutMapping("/{orderId}/accept")
	public ResponseEntity<OrderResponseDto> acceptOrder(
	        @PathVariable Long orderId) {

	    return ResponseEntity.ok(
	            orderService.acceptOrder(orderId));
	}
	
	
	@PutMapping("/{orderId}/reject")
	public ResponseEntity<OrderResponseDto> rejectOrder(
	        @PathVariable Long orderId) {

	    return ResponseEntity.ok(
	            orderService.rejectOrder(orderId));
	}
	
	@PutMapping("/{orderId}/preparing")
	public ResponseEntity<OrderResponseDto> markOrderAsPreparing(
	        @PathVariable Long orderId) {

	    return ResponseEntity.ok(
	            orderService.markOrderAsPreparing(orderId));
	}
	
	@PutMapping("/{orderId}/ready")
	public ResponseEntity<OrderResponseDto> markOrderAsReady(
	        @PathVariable Long orderId) {

	    return ResponseEntity.ok(
	            orderService.markOrderAsReady(orderId));
	}
	
	@PutMapping("/{orderId}/delivered")
	public ResponseEntity<OrderResponseDto> markOrderAsDelivered(
	        @PathVariable Long orderId) {

	    return ResponseEntity.ok(
	            orderService.markOrderAsDelivered(orderId));
	}
	
	
	
	/// - - - - - -  - -  - Admin APIs
	
	
	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> getAllOrders() {

	    return ResponseEntity.ok(
	            orderService.getAllOrders());
	}
	
	@GetMapping("/customer/admin/{customerId}")
	public ResponseEntity<List<OrderResponseDto>> getOrdersByCustomer(
	        @PathVariable Long customerId) {

	    return ResponseEntity.ok(
	            orderService.getOrdersByCustomer(customerId));
	}
	
	@GetMapping("/kitchen/{kitchenId}")
	public ResponseEntity<List<OrderResponseDto>> getOrdersByKitchen(
	        @PathVariable Long kitchenId) {

	    return ResponseEntity.ok(
	            orderService.getOrdersByKitchen(kitchenId));
	}
	
	
//	dashboard apis
	
	// total orders
	@GetMapping("/dashboard/total-orders")
	public ResponseEntity<Long> getTotalOrders() {

	    return ResponseEntity.ok(orderService.getTotalOrders());

	}
	
	@GetMapping("/dashboard/delivered-orders")
	public ResponseEntity<Long> getDeliveredOrders() {

	    return ResponseEntity.ok(orderService.getDeliveredOrders());

	}
	
	@GetMapping("/dashboard/cancelled-orders")
	public ResponseEntity<Long> getCancelledOrders() {

	    return ResponseEntity.ok(orderService.getCancelledOrders());

	}
	@GetMapping("/dashboard/pending-orders")
	public ResponseEntity<Long> getPendingOrders() {

	    return ResponseEntity.ok(orderService.getPendingOrders());

	}
	@GetMapping("/dashboard/total-revenue")
	public ResponseEntity<Double> getTotalRevenue() {

	    return ResponseEntity.ok(orderService.getTotalRevenue());

	}
	
	@GetMapping("/dashboard/admin-commission")
	public ResponseEntity<Double> getTotalAdminCommission() {

	    return ResponseEntity.ok(orderService.getTotalAdminCommission());

	}
	
	@GetMapping("/dashboard/kitchen-earnings")
	public ResponseEntity<Double> getTotalKitchenEarnings() {

	    return ResponseEntity.ok(orderService.getTotalKitchenEarnings());

	}
	
}
