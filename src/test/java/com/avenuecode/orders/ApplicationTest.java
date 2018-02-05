package com.avenuecode.orders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.repository.OrderRepository;
import com.avenuecode.orders.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.yml")
public class ApplicationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	private static boolean setUpDone = false;

	@Before
	public void setUp() {
		if (!setUpDone) {
			Order mockOrder = new Order("1", "RTL001", null, new BigDecimal(12.0), "SHIPPED");
			Order mockOrder2 = new Order("2", "RTL002", null, new BigDecimal(11.0), "DELAYED");
			Order mockOrder3 = new Order("4", "RTL004", new BigDecimal(10), new BigDecimal(10), "SHIPPED");
			Product product1 = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
			Product product2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(19.99));
			Product product3 = new Product("3", "1458843283", "7394750120000", "Floral Swing Skirt",
					new BigDecimal(69.99));
			mockOrder2.setProducts(Arrays.asList(product1, product2, product3));
			orderRepository.save(mockOrder);
			orderRepository.save(mockOrder2);
			orderRepository.save(mockOrder3);
			productRepository.save(product1);
			productRepository.save(product2);
			productRepository.save(product3);
		}
		setUpDone = true;
	}

	@Test
	public void testGetAllOrders() throws Exception {
		mvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].orderNumber", is("RTL001")));
	}

	@Test
	public void testGetOneOrder() throws Exception {
		mvc.perform(get("/orders/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.orderNumber", is("RTL002")));
	}

	@Test
	public void testSearchShippedOrders() {
		String status = "SHIPPED";
		try {
			mvc.perform(get("/search?status=" + status).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].status", is(status)))
					.andExpect(jsonPath("$[1].status", is(status)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testSearchOrdersWithDiscount() throws Exception {
		mvc.perform(get("/search/ordersWithDiscounts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].discount", is(10.0)));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testSearchOrdersWithMoreThanTwoProducts() throws Exception {
		Long number = (long) 2;
		mvc.perform(get("/search?ordersWithProductsMoreThan=" + number).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].products.length()").value(new GreaterThan(2)));

	}

	@Test
	public void testGetAllProducts() throws Exception {
		mvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].description", is("Diva Jeans")));
	}

	@Test
	public void testGetOneProduct() throws Exception {
		mvc.perform(get("/products/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.sku", is("9394550220002")));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testSearchProductsPricierThan30() throws Exception {
		BigDecimal number = new BigDecimal(30.0);
		mvc.perform(get("/search?productsWithPriceMoreThan=" + number).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].price").value(new GreaterThan(30.0)));

	}
}
