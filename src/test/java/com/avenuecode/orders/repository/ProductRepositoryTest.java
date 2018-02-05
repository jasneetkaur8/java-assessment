package com.avenuecode.orders.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.repository.ProductRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void testSearchProductsPricierThanLimit() {
		Product product1 = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product product2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(19.99));
		Product product3 = new Product("3", "1458843283", "7394750120000", "Floral Swing Skirt", new BigDecimal(69.99));
		Product product4 = new Product("4", "1358753283", "7394850130001", "Denim Short", new BigDecimal(29.99));
		entityManager.merge(product1);
		entityManager.merge(product2);
		entityManager.merge(product3);
		entityManager.merge(product4);
		entityManager.flush();
		List<Product> productsFound = productRepository.findByPriceGreaterThan(new BigDecimal(30));
		assertThat(productsFound.size()).isEqualTo(2);
		assertThat(productsFound.get(0).getPrice()).isGreaterThan(new BigDecimal(30));
		assertThat(productsFound.get(1).getPrice()).isGreaterThan(new BigDecimal(30));
	}

}
