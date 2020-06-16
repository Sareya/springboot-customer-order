package com.technotree.repositories;

import java.util.List;
import java.util.Optional; 

import org.springframework.data.repository.CrudRepository;

import com.technotree.models.order.Order;
import com.technotree.models.order.OrderStatus;


public interface OrderRepository extends CrudRepository<Order, Long>
{
	Optional<List<Order>> findByStatus(OrderStatus status);
}
