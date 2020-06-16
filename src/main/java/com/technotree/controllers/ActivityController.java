package com.technotree.controllers;

import java.util.List;   

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.technotree.models.order.Order;
import com.technotree.models.order.OrderStatus;
import com.technotree.models.user.User;
import com.technotree.services.OrderService;
import com.technotree.services.UserService;

@RestController
public class ActivityController 
{
	@Autowired
	UserService userService;
	@Autowired
	OrderService orderService;

	
	// USER RELATED ENDPOINTS
	
	@PostMapping("/register")
	public  <T> ResponseEntity<Object> register(@RequestBody User user)
	{
		return userService.createUser(user);
	}
	
	@GetMapping("/login")
	public User login(@RequestParam String username, @RequestParam String password )
	{
		return  userService.login(username, password);
	}
	
	@PutMapping("/users/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User updateUser)
	{
	    return userService.updateUserDetails(updateUser, id);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteCutomer(@PathVariable Long id)
	{
	   return userService.deleteUser(id);
	}
	
	@GetMapping("/users/{id}/all")
	public Iterable<User> getAllUsers(@PathVariable Long id)
	{
		return	userService.getAllUsers(id);
	}
	
	
	// USER OREDER RELATED ENDPOINTS	
	 
	@GetMapping("/users/{id}/orders")
	public List<Order> getUserOrder(@PathVariable Long id) 
	{
		return orderService.getOrderDetails(id);
	}
	 
	@PostMapping("/users/{id}/orders")
	public <T> ResponseEntity<Object> createOrder(@PathVariable Long id, @RequestBody Order order)
	{
		return orderService.createOrder(id, order);
	}
	
	@PutMapping("/users/{customerId}/orders/{orderId}")
	public Order updateOrder(@PathVariable Long customerId,
			@PathVariable Long orderId,
			@RequestBody Order updateOrder)
	{
		return orderService.updateOrderDetails(customerId, orderId, updateOrder);
	}
	
	@DeleteMapping("/users/{customerId}/orders/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable Long customerId, @PathVariable Long orderId)
	{
		return orderService.deleteOrder(customerId, orderId);
	}
	
	@PostMapping("/users/{customerId}/orders/{orderId}/cancel")
	public ResponseEntity<String> cancelOrder(@PathVariable Long customerId, @PathVariable Long orderId)
	{
		return orderService.changeOrderStatus(customerId, orderId, OrderStatus.CANCELLED);
	}
	
	@PostMapping("/users/{customerId}/orders/{orderId}/complete")
	public ResponseEntity<String> completeOrder(@PathVariable Long customerId, @PathVariable Long orderId)
	{
		return orderService.changeOrderStatus(customerId, orderId, OrderStatus.COMPLETED);
	}
	
	@GetMapping("/users/{id}/orders/all")
	public List<Order> getAllOrders(@PathVariable Long id) 
	{
		return orderService.getAllOrders(id);
	}
	
	@GetMapping("/users/{id}/orders/status/{orderstatus}")
	public List<Order> getAllOrders(@PathVariable Long id, @PathVariable OrderStatus orderstatus) 
	{
		return orderService.getOrdersByStaus(id, orderstatus);
	}
	 
}
