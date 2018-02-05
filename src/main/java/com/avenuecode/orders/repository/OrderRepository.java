package com.avenuecode.orders.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.avenuecode.orders.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Serializable> {
	public List<Order> findByStatus(String status);

	public List<Order> findByDiscountNotNull();

	@Query("Select o from Order AS o join o.products p Group By o.id Having count(p.id)>:number")
	public List<Order> findByNumberOfProducts(@Param("number") Long number);
}
