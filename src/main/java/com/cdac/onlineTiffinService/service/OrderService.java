package com.cdac.onlineTiffinService.service;

import java.util.List;

import com.cdac.onlineTiffinService.dto.OrderResponseDto;
import com.cdac.onlineTiffinService.dto.PlaceOrderRequestDto;
import com.cdac.onlineTiffinService.model.Status;

public interface OrderService {
	OrderResponseDto placeOrder(
	        Long customerId,
	        PlaceOrderRequestDto request);
	
	OrderResponseDto getOrderById(Long orderId);
	
	List<OrderResponseDto> getMyOrders(Long customerId);
	
	OrderResponseDto cancelOrder(Long orderId);
	
	List<OrderResponseDto> getMyOrdersByStatus(Long customerId,
            Status status);
	
	OrderResponseDto acceptOrder(Long orderId);
	
	OrderResponseDto rejectOrder(Long orderId);
	OrderResponseDto markOrderAsPreparing(Long orderId);
	OrderResponseDto markOrderAsReady(Long orderId);
	OrderResponseDto markOrderAsDelivered(Long orderId);
	List<OrderResponseDto> getAllOrders();
	List<OrderResponseDto> getOrdersByCustomer(Long customerId);
	List<OrderResponseDto> getOrdersByKitchen(Long kitchenId);
	Long getTotalOrders();
	
	Long getDeliveredOrders();
	Long getCancelledOrders();
	
	Long getPendingOrders();
	
	Double getTotalRevenue();
	
	Double getTotalAdminCommission();
	
	Double getTotalKitchenEarnings();
}
