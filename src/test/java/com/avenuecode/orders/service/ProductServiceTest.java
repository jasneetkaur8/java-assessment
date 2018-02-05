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

import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.repository.ProductRepository;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

	@TestConfiguration
	public static class ProductServiceTestContextConfiguration {

		@Bean
		public ProductService productService() {
			return new ProductService();
		}
	}

	@Autowired
	private ProductService productService;

	@MockBean
	private ProductRepository productRepository;

	@Before
	public void setUp() {
		Product product1 = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product product2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(19.99));
		Product product3 = new Product("3", "1458843283", "7394750120000", "Floral Swing Skirt", new BigDecimal(69.99));
		List<Product> allProducts = Arrays.asList(product1, product2, product3);

		Mockito.when(productRepository.findAll()).thenReturn(allProducts);
		Mockito.when(productRepository.findOne(product1.getProductId())).thenReturn(product1);
		Mockito.when(productRepository.findByPriceGreaterThan(new BigDecimal(30)))
				.thenReturn(Arrays.asList(product1, product3));
	}

	@Test
	public void returnAllFoundProducts() {
		List<Product> products = productService.listProducts();
		assertThat(products.size()).isEqualTo(3);
	}

	@Test
	public void returnOneProduct() {
		String productId = "1";
		Product product = productService.getProduct(productId);
		assertThat(product.getProductId()).isEqualTo(productId);
	}

	@Test
	public void returnProductsPricierThan30() {
		BigDecimal limit = new BigDecimal(30);
		List<Product> products = productService.getProductsWithPriceMoreThan(limit);
		assertThat(products.size()).isEqualTo(2);
		assertThat(products.get(0).getPrice()).isGreaterThan(limit);
		assertThat(products.get(1).getPrice()).isGreaterThan(limit);
	}

}
