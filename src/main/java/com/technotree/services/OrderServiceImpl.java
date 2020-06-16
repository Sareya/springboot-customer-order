package com.technotree.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.technotree.exceptions.ResourceNotFoundException;
import com.technotree.models.order.Order;
import com.technotree.models.order.OrderStatus;
import com.technotree.models.user.User;
import com.technotree.repositories.OrderRepository;
import com.technotree.repositories.UserRepository;

@Service
public class OrderServiceImpl implements OrderService
{
	
	private static final Logger LOG = LoggerFactory.getLogger( OrderService.class );
	
	@Autowired
	UserServiceImpl userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public List<Order> getAllOrders(Long adminId) 
	{
		LOG.info("User {} fetching all orders", adminId);
		return userService.isAdmin(adminId) ?
				(List<Order>) orderRepository.findAll():
				Collections.emptyList();
	}
	
	@Override
	public List<Order> getOrdersByStaus(Long adminId, OrderStatus status) 
	{	LOG.info("User {} fetching all orders by status {}", adminId, status);
		return userService.isAdmin(adminId) ?
				orderRepository.findByStatus(status)
				.orElseThrow(() -> new ResourceNotFoundException("orders", status)):
					Collections.emptyList();
	} 
	
	@Override
	public List<Order> getOrderDetails(Long customerId) 
	{
		User customer = userRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("customer", customerId));
		return customer.getOrders();
	}
	
	@Override
	public <T> ResponseEntity<Object> createOrder(Long customerId, Order order)
	{
		User customer = userRepository.findById(customerId)
				.orElseThrow( () -> new ResourceNotFoundException("customer", customerId) );
		order.setCustomer(customer);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(orderRepository.save(order));
	}
	
	@Override
	public Order updateOrderDetails(Long customerId, Long orderId, Order updateOrder)
	{
		return orderRepository.findById(orderId)
			      .map( order -> {
			    	  order.setCategory(updateOrder.getCategory());
			    	  order.setDescrition(updateOrder.getDescrition());
			    	  order.setPrice(updateOrder.getPrice());
			    	  order.setVat(updateOrder.getVat());
			    	  order.setUpdatedAt(new Date());
			    	  return orderRepository.save(order);
			      })
			      .orElseGet(() -> {
			    	  User customer = userService.getUserById(customerId);
			    	  updateOrder.setCustomer(customer);
			    	  updateOrder.setId(orderId);
			        return orderRepository.save(updateOrder);
			      });
	}
	
	@Override
	public ResponseEntity<String> changeOrderStatus(Long customerId, Long orderId, OrderStatus changeStatus)
	{
		Order order = getOrderById(orderId);
		OrderStatus currentStatus = order.getStatus();

		return currentStatus.equals(OrderStatus.IN_PROGRESS) ?
			 changeOrderStatus(order, customerId, changeStatus) :
				 methodNotAllowed(currentStatus, changeStatus);
				 
	}

	@Override
	public ResponseEntity<String> deleteOrder(Long customerId, Long orderId)
	{
		try
		{
			LOG.warn("Deleting order {} of customer {}", orderId, customerId);
			orderRepository.deleteById(orderId);
			LOG.info("Order {} of customer {} deleted", orderId, customerId);
		}
		catch(EmptyResultDataAccessException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order " + orderId + " for customer " + customerId + " found to be deleted");
		}
		return ResponseEntity.ok("Order " + orderId + " of customer " + customerId + " has been deleted");
	}
	
	private ResponseEntity<String> changeOrderStatus(Order order, Long customerId, OrderStatus nextstatus) 
	{
		OrderStatus currentStatus = order.getStatus();
		switch (nextstatus)
		{
			case COMPLETED:
				order.setStatus(OrderStatus.COMPLETED);
				break;
			case CANCELLED:
				order.setStatus(OrderStatus.CANCELLED);
				break;
			default:
				break;
		}
		orderRepository.save(order);
		LOG.info("Order {} status {} changed to {}", order.getId(), currentStatus, nextstatus);
		return ResponseEntity.
				ok("Order " + order.getId() + " of customer " + customerId + " has been " + nextstatus);
	}
	
	private ResponseEntity<String> methodNotAllowed(OrderStatus current, OrderStatus next )
	{
		return ResponseEntity
				 .status(HttpStatus.METHOD_NOT_ALLOWED)
				 .body("Method not allowed."
	      		+ " You can't " + next + " an order that is in the " + current + " status");
	}
	
	
	private Order getOrderById(Long id)
	{
		return orderRepository.findById(id)
				.orElseThrow( () -> new ResourceNotFoundException("order", id) );
	}
	
}
