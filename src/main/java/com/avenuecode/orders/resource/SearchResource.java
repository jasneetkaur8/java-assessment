package com.avenuecode.orders.resource;

import static org.springframework.http.ResponseEntity.ok;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.service.OrderService;
import com.avenuecode.orders.service.ProductService;

@RestController
@RequestMapping("/search")

public class SearchResource {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;

	@RequestMapping(params = { "status" })
	public ResponseEntity<List<Order>> sayHi(@RequestParam("status") String status) {
		return ok(orderService.searchByStatus(status));
	}

	@RequestMapping(value = "/ordersWithDiscounts")
	public ResponseEntity<List<Order>> orderWithDiscount() {
		return ok(orderService.searchOrdersWithDiscounts());
	}

	@RequestMapping(params = { "ordersWithProductsMoreThan" })
	public ResponseEntity<List<Order>> searchOrdersWithProductsMoreThan(
			@RequestParam("ordersWithProductsMoreThan") int number) {
		return ok(orderService.searchByNumberOfProducts((long) number));
	}

	@RequestMapping(params = { "productsWithPriceMoreThan" })
	public ResponseEntity<List<Product>> searchProductsWithPriceMoreThan(
			@RequestParam("productsWithPriceMoreThan") int number) {
		return ok(productService.getProductsWithPriceMoreThan(new BigDecimal(number)));
	}
}
