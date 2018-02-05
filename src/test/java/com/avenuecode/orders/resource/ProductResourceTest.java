package com.avenuecode.orders.resource;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.service.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductResource.class)
public class ProductResourceTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService productService;

	@Test
	public void returnListOfProducts() throws Exception {
		Product mockProduct = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product mockProduct2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(79.99));

		List<Product> allProducts = Arrays.asList(mockProduct, mockProduct2);
		given(productService.listProducts()).willReturn(allProducts);
		mvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].upc", is(mockProduct.getUpc())))
				.andExpect(jsonPath("$[1].description", is(mockProduct2.getDescription())));

	}

	@Test
	public void returnSingleProductWithId() throws Exception {
		Product mockProduct = new Product("1", "1257833283", "9394550220002", "Diva Jeans", new BigDecimal(39.99));
		Product mockProduct2 = new Product("2", "1358743283", "7394650110003", "Polo Shirt", new BigDecimal(79.99));

		String productId = mockProduct.getProductId();
		String productId2 = mockProduct2.getProductId();
		given(productService.getProduct(productId)).willReturn(mockProduct);
		given(productService.getProduct(productId2)).willReturn(mockProduct2);
		mvc.perform(get("/products/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.upc", is(mockProduct.getUpc())));
		mvc.perform(get("/products/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.description", is(mockProduct2.getDescription())));

	}

}
