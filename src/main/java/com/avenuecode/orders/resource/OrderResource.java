package com.avenuecode.orders.resource;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderResource {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public ResponseEntity<List<Order>> listOrders() {
		List<Order> orders = orderService.listOrders();
		orders.forEach(a -> System.out.println(a));
		return ok(orders);
	}

	@GetMapping(value = "/{orderId}")
	public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
		Order order = orderService.getOrder(orderId);
		if (order == null) {
			return notFound().build();
		}
		return ok(order);
	}

	// @GetMapping(value="/search")
	// public ResponseEntity<List<Order>> searchOrders(){
	// List<Order> orders = orderService.search("SHIPPED");
	// orders.forEach(a->System.out.println(a));
	// return ok(orders);
	// }

	// @GetMapping(value="/searchDisc")
	// public ResponseEntity<List<Order>> searchOrdersOnDiscount(){
	// List<Order> orders = orderService.searchIsNotNull();
	// orders.forEach(a->System.out.println(a));
	// return ok(orders);
	// }
	// @GetMapping(value="/searchProductNumber")
	// public ResponseEntity<List<Order>> searchOrdersOnStatusAndDiscount(){
	// List<Order> orders = orderService.searchByNumberOfProducts(2);
	// orders.forEach(a->System.out.println(a));
	// return ok(orders);
	// }

}
