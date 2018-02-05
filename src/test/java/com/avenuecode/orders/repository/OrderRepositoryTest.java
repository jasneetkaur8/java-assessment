package com.avenuecode.orders.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.repository.OrderRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	public void testSearchOrdersByStatus() {
		Order mockOrder = new Order("1", "RTL001", null, new BigDecimal(12.0), "SHIPPED");
		Order mockOrder2 = new Order("2", "RTL002", null, new BigDecimal(11.0), "DELAYED");
		Order mockOrder3 = new Order("3", "RTL003", null, new BigDecimal(11.0), "SHIPPED");
		entityManager.merge(mockOrder);
		entityManager.merge(mockOrder2);
		entityManager.merge(mockOrder3);
		entityManager.flush();
		List<Order> shippedOrders = orderRepository.findByStatus("SHIPPED");
		assertThat(shippedOrders.size()).isEqualTo(2);
		assertThat(shippedOrders.get(0).getStatus()).isEqualTo("SHIPPED");
	}

	@Test
	public void testSearchOrdersWithDiscounts() {
		Order mockOrder = new Order("1", "RTL001", null, new BigDecimal(12.0), "SHIPPED");
		Order mockOrder2 = new Order("2", "RTL002", new BigDecimal(10.0), new BigDecimal(11.0), "DELAYED");
		Order mockOrder3 = new Order("3", "RTL003", null, new BigDecimal(11.0), "SHIPPED");
		entityManager.merge(mockOrder);
		entityManager.merge(mockOrder2);
		entityManager.merge(mockOrder3);
		entityManager.flush();
		List<Order> ordersWithDiscounts = orderRepository.findByDiscountNotNull();
		assertThat(ordersWithDiscounts.size()).isEqualTo(1);
		assertThat(ordersWithDiscounts.get(0).getDiscount()).isEqualTo(new BigDecimal(10.0));
	}

	@Test
	public void testSearchOrdersWithMoreThan2Products() {
		Product product1 = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product product2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(19.99));
		Product product3 = new Product("3", "1458843283", "7394750120000", "Floral Swing Skirt", new BigDecimal(69.99));
		Product product4 = new Product("4", "1358753283", "7394850130001", "Denim Short", new BigDecimal(29.99));
		entityManager.merge(product1);
		entityManager.merge(product2);
		entityManager.merge(product3);
		entityManager.merge(product4);
		Order mockOrder = new Order("1", "RTL001", null, new BigDecimal(12.0), "SHIPPED");
		mockOrder.setProducts(Arrays.asList(product1));
		Order mockOrder2 = new Order("2", "RTL002", new BigDecimal(10.0), new BigDecimal(11.0), "DELAYED");
		mockOrder2.setProducts(Arrays.asList(product1, product2));
		Order mockOrder3 = new Order("3", "RTL003", null, new BigDecimal(11.0), "SHIPPED");
		mockOrder3.setProducts(Arrays.asList(product2, product3, product4));
		entityManager.merge(mockOrder);
		entityManager.merge(mockOrder2);
		entityManager.merge(mockOrder3);
		entityManager.flush();
		List<Order> ordersWithDiscounts = orderRepository.findByNumberOfProducts((long) 2);
		assertThat(ordersWithDiscounts.size()).isEqualTo(1);
		assertThat(ordersWithDiscounts.get(0).getProducts().size()).isEqualTo(3);
	}

}
