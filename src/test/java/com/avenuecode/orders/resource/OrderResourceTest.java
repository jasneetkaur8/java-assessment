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

import com.avenuecode.orders.domain.Order;
import com.avenuecode.orders.service.OrderService;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderResource.class)
public class OrderResourceTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private OrderService orderService;

	@Test
	public void returnListOfOrders() throws Exception {
		Order mockOrder = new Order("1", "RTL001", null, new BigDecimal(12.0), "SHIPPED");
		Order mockOrder2 = new Order("2", "RTL002", null, new BigDecimal(11.0), "DELAYED");
		Order mockOrder3 = new Order("4", "RTL004", new BigDecimal(10), new BigDecimal(10), "SHIPPED");
		List<Order> allOrders = Arrays.asList(mockOrder, mockOrder2, mockOrder3);
		given(orderService.listOrders()).willReturn(allOrders);
		mvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].orderId", is(mockOrder.getOrderId())))
				.andExpect(jsonPath("$[1].orderNumber", is(mockOrder2.getOrderNumber())))
				.andExpect(jsonPath("$[2].status", is(mockOrder3.getStatus())));

	}

	@Test
	public void returnSingleOrderWithId() throws Exception {
		Order mockOrder = new Order("1", "RTL001", null, new BigDecimal(12.0), "SHIPPED");
		Order mockOrder2 = new Order("2", "RTL002", null, new BigDecimal(11.0), "DELAYED");
		String orderId = mockOrder.getOrderId();
		String orderId2 = mockOrder2.getOrderId();
		given(orderService.getOrder(orderId)).willReturn(mockOrder);
		given(orderService.getOrder(orderId2)).willReturn(mockOrder2);
		mvc.perform(get("/orders/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.orderNumber", is(mockOrder.getOrderNumber())));
		mvc.perform(get("/orders/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(mockOrder2.getStatus())));

	}

}
