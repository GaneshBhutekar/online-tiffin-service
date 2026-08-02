package com.cdac.onlineTiffinService.service;


import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;

import com.cdac.onlineTiffinService.dto.OrderItemRequestDto;
import com.cdac.onlineTiffinService.dto.OrderItemResponseDto;
import com.cdac.onlineTiffinService.dto.OrderResponseDto;
import com.cdac.onlineTiffinService.dto.PlaceOrderRequestDto;
import com.cdac.onlineTiffinService.exceptions.BadRequestException;
import com.cdac.onlineTiffinService.exceptions.ResourceNotFoundException;
import com.cdac.onlineTiffinService.model.Kitchen;
import com.cdac.onlineTiffinService.model.MenuItem;
import com.cdac.onlineTiffinService.model.OrderItem;
import com.cdac.onlineTiffinService.model.Orders;
import com.cdac.onlineTiffinService.model.PaymentStatus;
import com.cdac.onlineTiffinService.model.Role;
import com.cdac.onlineTiffinService.model.Status;
import com.cdac.onlineTiffinService.model.User;
import com.cdac.onlineTiffinService.repository.KitchenRepository;
import com.cdac.onlineTiffinService.repository.MenuRepository;
import com.cdac.onlineTiffinService.repository.OrderRepository;
import com.cdac.onlineTiffinService.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor


public class OrderServiceImpl  implements OrderService{
	private final OrderRepository orderRepository;
	
    private final UserRepository userRepository;
    
    
    private final KitchenRepository kitchenRepository;

    private final MenuRepository menuRepository;

    private final EmailService emailService;

//    private ModelMapper modelMapper;

    // sends an order-status notification email to both the customer and the kitchen owner
    private void notifyCustomerAndKitchen(Orders order, String subjectPrefix, String extraMessage) {
        try {
            String customerEmail = order.getCustomer().getEmail();
            String customerName = order.getCustomer().getName();
            String kitchenOwnerEmail = order.getKitchen().getOwner().getEmail();
            String kitchenOwnerName = order.getKitchen().getOwner().getName();
            String kitchenName = order.getKitchen().getKitchenName();

            emailService.sendEmail(
                    customerEmail,
                    subjectPrefix + " - Order #" + order.getId(),
                    "Hi " + customerName + ",\n\n"
                            + "Your order #" + order.getId() + " from \"" + kitchenName + "\" " + extraMessage + "\n\n"
                            + "Total: Rs. " + order.getTotalPrice() + "\n\n"
                            + "Thanks,\nOnline Tiffin Service Team"
            );

            emailService.sendEmail(
                    kitchenOwnerEmail,
                    subjectPrefix + " - Order #" + order.getId(),
                    "Hi " + kitchenOwnerName + ",\n\n"
                            + "Order #" + order.getId() + " placed by " + customerName + " " + extraMessage + "\n\n"
                            + "Total: Rs. " + order.getTotalPrice() + "\n\n"
                            + "Thanks,\nOnline Tiffin Service Team"
            );
        } catch (Exception e) {
            // never let an email failure roll back the order status change
            System.err.println("Failed to send order notification email: " + e.getMessage());
        }
    }
    
    @Override
    public OrderResponseDto placeOrder(Long customerId,PlaceOrderRequestDto request) {
    	User customer = userRepository.findById(customerId)
    	        .orElseThrow(() ->
    	                new ResourceNotFoundException("User", "Id", customerId));
    	if (customer.getRole() != Role.CUSTOMER) {
    	    throw new BadRequestException("Only customers can place orders");
    	}
    	Kitchen kitchen = kitchenRepository.findById(request.getKitchenId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Kitchen" , "id" , request.getKitchenId()));
    	Orders order = new Orders();
    	
    	order.setCustomer(customer);
        order.setKitchen(kitchen);
        order.setStatus(Status.PLACED);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        
        double totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        
        if (request.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }
        // loop start to add the items in menu item table , for this order which is dealt by customer id with kitchen gthis
        
        for(OrderItemRequestDto dto : request.getItems()) {
        	MenuItem menuItem = menuRepository
        	        .findByIdAndAvailableTrue(dto.getMenuItemId())
        	        .orElseThrow(() ->
        	                new ResourceNotFoundException("Menu item" , "id" , dto.getMenuItemId()));
        	if (!menuItem.getKitchen().getId().equals(kitchen.getId())) {
        		// if menu item kutchen id is not same as kitchen id we got from the client
        	    throw new BadRequestException(
        	            "Menu item does not belong to selected kitchen");
        	}
        	
        	OrderItem orderItem = new OrderItem();
        	orderItem.setOrder(order);

        	orderItem.setMenuItem(menuItem);

        	orderItem.setQuantity(dto.getQuantity());

        	orderItem.setPrice(menuItem.getPrice());
        	double subTotal =
        	        menuItem.getPrice() * dto.getQuantity();
        	totalPrice += subTotal;
        	orderItems.add(orderItem);
        	
        }
        
        order.setOrderItems(orderItems); // the list of order item is being added in the order so that each order can have many order items , which is from
        // one kitchen 
        
        order.setTotalPrice(totalPrice);
        double commission = totalPrice * 0.10;

        order.setAdminCommission(commission);

        order.setKitchenAmount(totalPrice - commission);
        
        Orders savedOrder = orderRepository.save(order);
        
//        return modelMapper.map(savedOrder, OrderResponseDto.class); // too many problems mistmatches between this both
        

        return mapToResponse(savedOrder);
        
    }
    
    
    
    @Override
    public OrderResponseDto getOrderById(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("order" , "id" , orderId));

        return mapToResponse(order);
    }
    
    
    
    @Override
    public List<OrderResponseDto> getMyOrders(Long customerId) {

        // Check whether the customer exists
        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", customerId));

        // Ensure only customers can access this API
        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer");
        }

        List<Orders> orders =
                orderRepository.findByCustomer_IdOrderByOrderDateDesc(customerId);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public OrderResponseDto cancelOrder(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order", "Id", orderId));

        if (order.getStatus() == Status.PREPARING ||
            order.getStatus() == Status.READY ||
            order.getStatus() == Status.DELIVERED) {

            throw new BadRequestException(
                    "Order cannot be cancelled once preparation has started.");
        }

        if (order.getStatus() == Status.CANCELLED) {

            throw new BadRequestException(
                    "Order is already cancelled.");
        }

        order.setStatus(Status.CANCELLED);

        Orders updatedOrder = orderRepository.save(order);

        notifyCustomerAndKitchen(updatedOrder, "Order Cancelled",
                "has been cancelled.");

        return mapToResponse(updatedOrder);
    }
    
    @Override
    public List<OrderResponseDto> getMyOrdersByStatus(Long customerId,
                                                      Status status) {

        // Check whether the customer exists
        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", customerId));

        // Validate role
        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        List<Orders> orders =
                orderRepository.findByCustomer_IdAndStatusOrderByOrderDateDesc(
                        customerId, status);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public OrderResponseDto acceptOrder(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order", "Id", orderId));

        if (order.getStatus() != Status.PLACED) {
            throw new BadRequestException(
                    "Only placed orders can be accepted.");
        }

        order.setStatus(Status.ACCEPTED);

        Orders updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }
    
    @Override
    public OrderResponseDto rejectOrder(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order", "Id", orderId));

        if (order.getStatus() != Status.PLACED) {
            throw new BadRequestException(
                    "Only placed orders can be rejected.");
        }

        order.setStatus(Status.CANCELLED);

        Orders updatedOrder = orderRepository.save(order);

        notifyCustomerAndKitchen(updatedOrder, "Order Cancelled",
                "has been rejected by the kitchen and cancelled.");

        return mapToResponse(updatedOrder);
    }
    
    @Override
    public OrderResponseDto markOrderAsPreparing(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order", "Id", orderId));

        if (order.getStatus() != Status.ACCEPTED) {
            throw new BadRequestException(
                    "Only accepted orders can be marked as preparing.");
        }

        order.setStatus(Status.PREPARING);

        Orders updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }
    
    @Override
    public OrderResponseDto markOrderAsReady(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order", "Id", orderId));

        if (order.getStatus() != Status.PREPARING) {
            throw new BadRequestException(
                    "Only preparing orders can be marked as ready.");
        }

        order.setStatus(Status.READY);

        Orders updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }	
    
    @Override
    public OrderResponseDto markOrderAsDelivered(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order", "Id", orderId));

        if (order.getStatus() != Status.READY) {
            throw new BadRequestException(
                    "Only ready orders can be marked as delivered.");
        }

        order.setStatus(Status.DELIVERED);

        Orders updatedOrder = orderRepository.save(order);

        notifyCustomerAndKitchen(updatedOrder, "Order Delivered",
                "has been delivered successfully.");

        return mapToResponse(updatedOrder);
    }
    
    @Override
    public List<OrderResponseDto> getAllOrders() {

        List<Orders> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public List<OrderResponseDto> getOrdersByCustomer(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", customerId));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        List<Orders> orders =
                orderRepository.findByCustomer_IdOrderByOrderDateDesc(customerId);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public List<OrderResponseDto> getOrdersByKitchen(Long kitchenId) {

        Kitchen kitchen = kitchenRepository.findById(kitchenId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Kitchen", "Id", kitchenId));

        List<Orders> orders =
                orderRepository.findByKitchen_IdOrderByOrderDateDesc(kitchenId);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public Long getTotalOrders() {

        return orderRepository.count(); // select count(*) from orders;

    }
    
    @Override
    public Long getDeliveredOrders() {

        return orderRepository.countByStatus(Status.DELIVERED);

    }
    
    
    @Override
    public Long getCancelledOrders() {

        return orderRepository.countByStatus(Status.CANCELLED);

    }
    @Override
    public Long getPendingOrders() {

        return orderRepository.countByStatus(Status.PREPARING);

    }
    @Override
    public Double getTotalRevenue() {

        return orderRepository.getTotalRevenue();

    }
    
    @Override
    public Double getTotalAdminCommission() {

        return orderRepository.getTotalAdminCommission();

    }
    @Override
    public Double getTotalKitchenEarnings() {

        return orderRepository.getTotalKitchenEarnings();

    }
    
    
    
    
    
    
    
    
    private OrderResponseDto mapToResponse(Orders order) {

        OrderResponseDto response = new OrderResponseDto();

        response.setOrderId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setKitchenId(order.getKitchen().getId());
        response.setTotalPrice(order.getTotalPrice());
        response.setAdminCommission(order.getAdminCommission());
        response.setKitchenAmount(order.getKitchenAmount());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());

        List<OrderItemResponseDto> itemDtos = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemResponseDto itemDto = new OrderItemResponseDto();

            itemDto.setId(item.getMenuItem().getId());
            itemDto.setMenuItemName(item.getMenuItem().getDishName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());

            itemDtos.add(itemDto);
        }

        response.setItems(itemDtos);

        return response;
    }
    
    
}
