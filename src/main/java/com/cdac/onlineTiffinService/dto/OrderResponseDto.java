package com.cdac.onlineTiffinService.dto;




import java.time.LocalDateTime;
import java.util.List;

import com.cdac.onlineTiffinService.model.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    
    private Long customerId;
    
    private Long kitchenId;
    
    private double totalPrice;
    
    private double adminCommission;

    private double kitchenAmount;

    private Status status;

    private LocalDateTime orderDate;

    private List<OrderItemResponseDto> items;
    
    
}
