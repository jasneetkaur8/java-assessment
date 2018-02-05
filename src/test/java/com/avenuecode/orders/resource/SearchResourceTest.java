package com.avenuecode.orders.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.service.OrderService;
import com.avenuecode.orders.service.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchResource.class)

public class SearchResourceTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private OrderService orderService;
	@MockBean
	private ProductService productService;

	Order mockOrder, mockOrder2;

	@Before
	public void setUp() {
		mockOrder = new Order("1", "RTL001", new BigDecimal(10), new BigDecimal(12.0), "SHIPPED");
		mockOrder2 = new Order("2", "RTL002", new BigDecimal(15), new BigDecimal(11.0), "SHIPPED");
	}

	@Test
	public void testSearchOrdersByStatus() throws Exception {
		// Order mockOrder = new Order("1","RTL001", null,new
		// BigDecimal(12.0),"SHIPPED");
		// Order mockOrder2 = new Order("2","RTL002", null,new
		// BigDecimal(11.0),"SHIPPED");
		List<Order> shippedOrders = Arrays.asList(mockOrder, mockOrder2);
		given(orderService.searchByStatus(mockOrder.getStatus())).willReturn(shippedOrders);
		mvc.perform(get("/search?status=SHIPPED").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].status", is("SHIPPED")))
				.andExpect(jsonPath("$[1].status", is("SHIPPED")));
	}

	@Test
	public void testSearchOrdersHavingDiscount() throws Exception {
		List<Order> ordersWithDiscounts = Arrays.asList(mockOrder, mockOrder2);
		given(orderService.searchOrdersWithDiscounts()).willReturn(ordersWithDiscounts);
		mvc.perform(get("/search/ordersWithDiscounts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testSearchOrdersWithProductsMoreThanALimit() throws Exception {
		Product product1 = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product product2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(19.99));
		Product product3 = new Product("3", "1458843283", "7394750120000", "Floral Swing Skirt", new BigDecimal(69.99));
		Product product4 = new Product("4", "1358753283", "7394850130001", "Denim Short", new BigDecimal(29.99));

		mockOrder.getProducts().add(product2);
		mockOrder2.getProducts().add(product3);
		mockOrder2.getProducts().add(product4);
		mockOrder2.getProducts().add(product1);

		List<Order> ordersWithProducts = Arrays.asList(mockOrder2);
		given(orderService.searchByNumberOfProducts((long) 2)).willReturn(ordersWithProducts);
		mvc.perform(get("/search?ordersWithProductsMoreThan=2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].products.length()").value(new GreaterThan(2)));
	}

	@Test
	public void testSearchProductsWithPriceMoreThanALimit() throws Exception {
		Product mockProduct1 = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product mockProduct2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(69.99));
		List<Product> ProductsPricierThanThirty = Arrays.asList(mockProduct1, mockProduct2);
		given(productService.getProductsWithPriceMoreThan(new BigDecimal(30))).willReturn(ProductsPricierThanThirty);
		mvc.perform(get("/search?productsWithPriceMoreThan=30").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].price", is(new BigDecimal(39.99))))
				.andExpect(jsonPath("$[1].price", is(new BigDecimal(69.99))));
	}

}
