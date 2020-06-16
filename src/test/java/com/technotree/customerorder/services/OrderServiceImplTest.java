package com.technotree.customerorder.services;

import static org.junit.Assert.*; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.technotree.customerorder.TestUtils;
import com.technotree.exceptions.ResourceNotFoundException;
import com.technotree.models.order.Order;
import com.technotree.models.order.OrderStatus;
import com.technotree.models.user.User;
import com.technotree.repositories.OrderRepository;
import com.technotree.repositories.UserRepository;
import com.technotree.services.OrderServiceImpl;
import com.technotree.services.UserServiceImpl;

@SpringBootTest
public class OrderServiceImplTest
{
	@InjectMocks
	OrderServiceImpl orderService;
	@Mock
	UserServiceImpl userService;
	@Mock
	UserRepository userRepository;
	@Mock
	OrderRepository orderRepository;
	
	@Before
	public void setUp() 
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getAllOrders_admin_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long adminId = 1L;
		Long orderId = 2L;
		List<Order> orders = getOrders(orderId);
		Mockito.when(userService.isAdmin(adminId)).thenReturn(true);
		Mockito.when(orderRepository.findAll()).thenReturn(orders);
		
		List<Order> allOrders = orderService.getAllOrders(adminId);
		assertEquals(orders, allOrders);
	}
	
	@Test
	public void getAllOrders_customer_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long adminId = 1L;
		Mockito.when(userService.isAdmin(adminId)).thenReturn(false);
		
		List<Order> allOrders = orderService.getAllOrders(adminId);
		assertEquals(Collections.EMPTY_LIST, allOrders);
	}
	
	@Test
	public void getOrdersByStatus_ok_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long adminId = 1L;
		User user = TestUtils.getUser();
		List<Order> orders = getOrders(2L);
		user.setOrders(orders);
		Mockito.when(userService.isAdmin(adminId)).thenReturn(true);
		Mockito.when(orderRepository.findByStatus(OrderStatus.IN_PROGRESS)).thenReturn(Optional.of(orders));
		
		List<Order> allOrders = orderService.getOrdersByStaus(adminId, OrderStatus.IN_PROGRESS);
		assertEquals(orders, allOrders);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void getOrdersByStatus_exception_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long adminId = 1L;
		User user = TestUtils.getUser();
		List<Order> orders = getOrders(2L);
		user.setOrders(orders);
		Mockito.when(userService.isAdmin(adminId)).thenReturn(true);
		Mockito.when(orderRepository.findByStatus(OrderStatus.IN_PROGRESS)).
			thenThrow(ResourceNotFoundException.class);
		
		List<Order> allOrders = orderService.getOrdersByStaus(adminId, OrderStatus.IN_PROGRESS);
	}
	
	@Test
	public void getOrderDetails_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long adminId = 1L;
		User user = TestUtils.getUser();
		List<Order> orders = getOrders(2L);
		user.setOrders(orders);
		Mockito.when(userRepository.findById(adminId)).thenReturn(Optional.of(user));
		
		List<Order> allOrders = orderService.getOrderDetails(adminId);
		assertEquals(orders, allOrders);
	}
	
	@Test
	public void createOrder_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long customerId = 1L;
		User customer = TestUtils.getUser();
		customer.setId(customerId);
		Order order = getOrder(2L);
		order.setCustomer(customer);
		Mockito.when(userRepository.findById(customerId)).thenReturn(Optional.of(customer));
		Mockito.when(orderRepository.save(order)).thenReturn(order);
		
		ResponseEntity<Object> actualResponse = orderService.createOrder(customerId, order);
		ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.CREATED).body(order);
		assertEquals(response, actualResponse);
	}
	
	@Test
	public void updateOrderDetails_test() throws JsonParseException, JsonMappingException, IOException
	{
		Float price = 200F;
		Order updateOrder = new Order();
		updateOrder.setPrice(price);
		Long customerId = 1L;
		Long orderId = 2L;
		
		User customer = TestUtils.getUser();
		customer.setId(customerId);
		
		Order order = getOrder(orderId);
		order.setCustomer(customer);
		
		Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		order.setPrice(price);
		Mockito.when(orderRepository.save(order)).thenReturn(order);
		
		Order actualOrder = orderService.updateOrderDetails(customerId, orderId, updateOrder);
		assertEquals(order, actualOrder);
	}
	
	@Test
	public void changeOrderStatus_complete_test() throws JsonParseException, JsonMappingException, IOException
	{
		changeOrderStatus_mocks(OrderStatus.COMPLETED);
	}
	
	public void changeOrderStatus_cancel_test() throws JsonParseException, JsonMappingException, IOException
	{
		changeOrderStatus_mocks(OrderStatus.CANCELLED);
	}
	
	private void changeOrderStatus_mocks(OrderStatus status) throws JsonParseException, JsonMappingException, IOException
	{
		Long customerId = 1L;
		User customer = TestUtils.getUser();
		customer.setId(customerId);
		
		Long orderId = 2L;
		Order order = getOrder(orderId);
		order.setCustomer(customer);
		Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		Mockito.when(orderRepository.save(order)).thenReturn(order);
		
		ResponseEntity<String> actualResponse = orderService.changeOrderStatus(customerId, orderId, status);
		ResponseEntity<String> response = ResponseEntity.ok("Order " + orderId + " of customer " + customerId + " has been " + status);
		
		assertEquals(response, actualResponse);
	}
	
	@Test
	public void changeOrderStatus_nok_test() throws JsonParseException, JsonMappingException, IOException
	{
		OrderStatus status = OrderStatus.COMPLETED;
		
		Long customerId = 1L;
		User customer = TestUtils.getUser();
		customer.setId(customerId);
		
		Long orderId = 2L;
		Order order = getOrder(orderId);
		order.setCustomer(customer);
		order.setStatus(status);
		
		Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
		
		ResponseEntity<String> actualResponse = orderService.changeOrderStatus(customerId, orderId, status);
		ResponseEntity<String> response = ResponseEntity
				 .status(HttpStatus.METHOD_NOT_ALLOWED)
				 .body("Method not allowed."
	      		+ " You can't " + status + " an order that is in the " + status + " status");
		assertEquals(response, actualResponse);
	}
	
	@Test
	public void deleteOrder_test() throws JsonParseException, JsonMappingException, IOException
	{
		
		Long customerId = 1L;
		Long orderId = 2L;
		
		Mockito.doNothing().when(orderRepository).deleteById(Mockito.isA(Long.class));
		
		ResponseEntity<String> actualResponse = orderService.deleteOrder(customerId, orderId);
		ResponseEntity<String> response = ResponseEntity.ok("Order " + orderId + " of customer " + customerId + " has been deleted");
		
		assertEquals(response, actualResponse);
	}
	
	@Test
	public void deleteOrder_exception_test() throws JsonParseException, JsonMappingException, IOException
	{
		
		Long customerId = 1L;
		Long orderId = 2L;
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(orderRepository).deleteById(Mockito.isA(Long.class));
		
		ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order " + orderId + " for customer " + customerId + " found to be deleted");
		ResponseEntity<String> actualResponse = orderService.deleteOrder(customerId, orderId);
		assertEquals(response, actualResponse);
	}
	
	private List<Order> getOrders(Long orderId) throws JsonParseException, JsonMappingException, IOException
	{
		List<Order> orders = new ArrayList<>();
		orders.add(getOrder(orderId));
		return orders;
	}
	
	
	private Order getOrder(Long orderId) throws JsonParseException, JsonMappingException, IOException
	{
		Order order = TestUtils.getOrder();
		order.setId(2L);
		return order;
	}
	
	
}
