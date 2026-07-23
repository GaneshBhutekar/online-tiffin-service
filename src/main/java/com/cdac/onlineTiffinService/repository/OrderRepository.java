package com.cdac.onlineTiffinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.onlineTiffinService.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>{

}
