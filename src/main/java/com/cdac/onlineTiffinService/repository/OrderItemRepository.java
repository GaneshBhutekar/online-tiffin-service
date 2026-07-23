package com.cdac.onlineTiffinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.onlineTiffinService.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
