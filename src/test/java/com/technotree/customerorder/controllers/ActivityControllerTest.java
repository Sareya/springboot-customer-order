package com.technotree.customerorder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.technotree.controllers.ActivityController;
import com.technotree.customerorder.TestUtils;
import com.technotree.models.order.Order;
import com.technotree.models.order.OrderStatus;
import com.technotree.models.user.User;
import com.technotree.services.OrderService;
import com.technotree.services.UserService;


@RunWith(SpringRunner.class)
@WebMvcTest(ActivityController.class)
public class ActivityControllerTest 
{
	@Autowired
    private MockMvc myMockMvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private OrderService orderService;
	

	@Test
	public void register_test() throws Exception
	{
		User user = TestUtils.getUser();
		String content = TestUtils.getString(user);
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
                
		myMockMvc.perform(mockHttpServletRequestBuilder)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(userService).createUser(user);
	}
	
	@Test
	public void login_test() throws Exception
	{
		String username = "test";
		String password = "test";
		myMockMvc.perform(get("/login")
				.queryParam("username", username)
				.queryParam("password", password))
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(userService).login(username, password);
	}
	
	@Test
	public void updateCustomer_test() throws Exception
	{
		Long userId = 1L;
		User user = TestUtils.getUser();
		String content = TestUtils.getString(user);
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.put("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);;
		
		myMockMvc.perform(mockHttpServletRequestBuilder)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(userService).updateUserDetails(user, userId);
	}
	
	@Test
	public void deleteCustomer_test() throws Exception
	{
		Long userId = 1L;
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.delete("/users/" + userId);
		
		myMockMvc.perform(mockHttpServletRequestBuilder)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(userService).deleteUser(userId);
	}
	
	@Test
	public void getAllUsers_test() throws Exception
	{
		Long adminId = 1L;
		myMockMvc.perform(get("/users/" + adminId + "/all"))
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(userService).getAllUsers(adminId);
	}
	
	@Test
	public void getUserOrder_test() throws Exception
	{
		Long customerId = 1L;
		myMockMvc.perform(get("/users/" + customerId + "/orders"))
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(orderService).getOrderDetails(customerId);
	}
	
	@Test
	public void createOrder_test() throws Exception
	{
		Long customerId = 1L;
		Order order = TestUtils.getOrder();
		String content = TestUtils.getString(order);
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/users/" + customerId + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
                
		myMockMvc.perform(mockHttpServletRequestBuilder)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(orderService).createOrder(customerId, order);
	}
	
	@Test
	public void updateOrder_test() throws Exception
	{
		Long customerId = 1L;
		Long orderId = 2L;
		Order order = TestUtils.getOrder();
		String content = TestUtils.getString(order);
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.put("/users/" + customerId + "/orders/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
                
		myMockMvc.perform(mockHttpServletRequestBuilder)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(orderService).updateOrderDetails(customerId, orderId, order);
	}
	
	@Test
	public void deleteOrder_test() throws Exception
	{
		Long customerId = 1L;
		Long orderId = 2L;
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.delete("/users/" + customerId + "/orders/" + orderId);
		
		myMockMvc.perform(mockHttpServletRequestBuilder)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(orderService).deleteOrder(customerId, orderId);
	}
	
	@Test
	public void cancelOrder_test() throws Exception
	{
		changeOrder_test(OrderStatus.CANCELLED, "cancel");
	}
	
	@Test
	public void completeOrder_test() throws Exception
	{
		changeOrder_test(OrderStatus.COMPLETED, "complete");
	}
	
	@Test
	public void getAllOrders_test() throws Exception
	{
		Long adminId = 1L;
		myMockMvc.perform(get("/users/" + adminId + "/orders/all"))
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(orderService).getAllOrders(adminId);
	}
	
	@Test
	public void getAllOrdersByStatus_test() throws Exception
	{
		Long adminId = 1L;
		OrderStatus status = OrderStatus.IN_PROGRESS;
		myMockMvc.perform(get("/users/" + adminId + "/orders/status/" + status))
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(orderService).getOrdersByStaus(adminId, status);
	}
	
	private void changeOrder_test(OrderStatus status, String path) throws Exception
	{
		Long customerId = 1L;
		Long orderId = 2L;
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/users/" + customerId + "/orders/" + orderId +"/" + path);
                
		myMockMvc.perform(mockHttpServletRequestBuilder)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		Mockito.verify(orderService).changeOrderStatus(customerId, orderId, status);
	}
	
	
}
