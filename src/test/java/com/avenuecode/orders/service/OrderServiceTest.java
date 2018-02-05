package com.avenuecode.orders.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.repository.OrderRepository;
import com.avenuecode.orders.service.OrderService;

@RunWith(SpringRunner.class)
public class OrderServiceTest {

	@TestConfiguration
	public static class OrderServiceTestContextConfiguration {

		@Bean
		public OrderService orderService() {
			return new OrderService();
		}
	}

	@Autowired
	private OrderService orderService;

	@MockBean
	private OrderRepository orderRepository;

	@Before
	public void setUp() {
		Order mockOrder = new Order("1", "RTL001", null, new BigDecimal(12.0), "SHIPPED");
		Order mockOrder2 = new Order("2", "RTL002", null, new BigDecimal(11.0), "DELAYED");
		Order mockOrder3 = new Order("4", "RTL004", new BigDecimal(10), new BigDecimal(10), "SHIPPED");
		Product product1 = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product product2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(19.99));
		Product product3 = new Product("3", "1458843283", "7394750120000", "Floral Swing Skirt", new BigDecimal(69.99));
		mockOrder2.setProducts(Arrays.asList(product1, product2, product3));
		List<Order> allOrders = Arrays.asList(mockOrder, mockOrder2, mockOrder3);

		Mockito.when(orderRepository.findAll()).thenReturn(allOrders);
		Mockito.when(orderRepository.findOne(mockOrder.getOrderId())).thenReturn(mockOrder);
		Mockito.when(orderRepository.findByStatus(mockOrder3.getStatus()))
				.thenReturn(Arrays.asList(mockOrder, mockOrder3));
		Mockito.when(orderRepository.findByDiscountNotNull()).thenReturn(Arrays.asList(mockOrder3));
		Mockito.when(orderRepository.findByNumberOfProducts((long) 2)).thenReturn(Arrays.asList(mockOrder2));
	}

	@Test
	public void returnAllFoundOrders() {
		List<Order> orders = orderService.listOrders();

		assertThat(orders.size()).isEqualTo(3);
	}

	@Test
	public void returnOneOrder() {
		String orderId = "1";
		Order order = orderService.getOrder(orderId);
		assertThat(order.getOrderId()).isEqualTo(orderId);
	}

	@Test
	public void returnShippedOrders() {
		String status = "SHIPPED";
		List<Order> orders = orderService.searchByStatus(status);
		assertThat(orders.size()).isEqualTo(2);
		assertThat(orders.get(0).getStatus()).isEqualTo(status);
		assertThat(orders.get(1).getStatus()).isEqualTo(status);
	}

	@Test
	public void returnOrdersWithDiscount() {
		List<Order> orders = orderService.searchOrdersWithDiscounts();
		assertThat(orders.size()).isEqualTo(1);
		assertThat(orders.get(0).getDiscount()).isEqualTo(new BigDecimal(10));
	}

	@Test
	public void returnOrdersWithMoreThanTwoProducts() {
		Long number = (long) 2;
		List<Order> orders = orderService.searchByNumberOfProducts(number);
		assertThat(orders.size()).isEqualTo(1);
		assertThat(orders.get(0).getProducts().size()).isGreaterThan(2);
	}

}
