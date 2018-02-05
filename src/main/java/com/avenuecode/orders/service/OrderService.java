package com.avenuecode.orders.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public List<Order> listOrders() {
		return orderRepository.findAll();
	}

	public Order getOrder(String orderId) {
		return orderRepository.findOne(orderId);
	}

	public List<Order> searchByStatus(String status) {
		return orderRepository.findByStatus(status);
	}

	public List<Order> searchOrdersWithDiscounts() {
		return orderRepository.findByDiscountNotNull();
	}

	public List<Order> searchByNumberOfProducts(Long value) {
		return orderRepository.findByNumberOfProducts(value);
	}

}
