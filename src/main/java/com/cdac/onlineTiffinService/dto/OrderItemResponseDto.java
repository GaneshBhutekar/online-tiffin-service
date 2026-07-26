package com.cdac.onlineTiffinService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OrderItemResponseDto {
    private Long id;

    private String menuItemName;

    private int quantity;

    private double price;
}
