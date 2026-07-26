package com.cdac.onlineTiffinService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cdac.onlineTiffinService.model.Orders;
import com.cdac.onlineTiffinService.model.Status;

public interface OrderRepository extends JpaRepository<Orders, Long>{
	List<Orders> findByCustomer_IdOrderByOrderDateDesc(Long customerId);
	List<Orders> findByCustomer_IdAndStatusOrderByOrderDateDesc(Long customerId,
            Status status);
	
	List<Orders> findByKitchen_IdOrderByOrderDateDesc(Long kitchenId);
	
	long countByStatus(Status status); // select count(*) from orders where status = 'DELIVERED' ||CANCELLLED
	
	@Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Orders o")
	Double getTotalRevenue();
	
	@Query("SELECT COALESCE(SUM(o.adminCommission), 0) FROM Orders o")
	Double getTotalAdminCommission();
	
	@Query("SELECT COALESCE(SUM(o.kitchenAmount), 0) FROM Orders o")
	Double getTotalKitchenEarnings();
	
	
}
