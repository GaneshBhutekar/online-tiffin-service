package com.cdac.onlineTiffinService.dto;

import java.util.List;

import com.cdac.onlineTiffinService.model.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PlaceOrderRequestDto {
	@NotNull
	private Long kitchenId;
	
	
	@NotNull
    private PaymentMethod paymentMethod;
	
	
	@Valid
	@NotEmpty
	private List<OrderItemRequestDto> items;
}
