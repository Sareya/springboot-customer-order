package com.technotree.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.technotree.models.order.Order;
import com.technotree.models.order.OrderStatus;

public interface OrderService 
{
	List<Order> getAllOrders(Long adminId);
	List<Order> getOrdersByStaus(Long adminId, OrderStatus status);
	List<Order> getOrderDetails(Long customerId);
	<T> ResponseEntity<Object> createOrder(Long customerId, Order order);
	Order updateOrderDetails(Long customerId, Long orderId, Order updateOrder);
	ResponseEntity<String> changeOrderStatus(Long customerId, Long orderId, OrderStatus changeStatus);
	ResponseEntity<String> deleteOrder(Long customerId, Long orderId);
}
