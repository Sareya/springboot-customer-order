package com.technotree.customerorder;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technotree.models.order.Category;
import com.technotree.models.order.Order;
import com.technotree.models.order.OrderStatus;
import com.technotree.models.user.Gender;
import com.technotree.models.user.Role;
import com.technotree.models.user.User;

public class TestUtils 
{
	public static User getUser() throws JsonParseException, JsonMappingException, IOException
	{
		User user = new User();
		user.setUsername("testUsername");
		user.setPassword("testPassword");
		user.setFullname("test test");
		user.setGender(Gender.MALE);
		user.setPhone("111-1111111");
		user.setRole(Role.CUSTOMER);
		
		return user;
	}
	
	public static User getAdminUser() throws JsonParseException, JsonMappingException, IOException
	{
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setAdminSecretKey("verysecretkey");
		user.setFullname("admin admin");
		user.setGender(Gender.MALE);
		user.setPhone("111-1111111");
		user.setRole(Role.ADMIN);
		
		return user;
	}
	
	public static Order getOrder() throws JsonParseException, JsonMappingException, IOException
	{
		Order order = new Order();
		order.setCategory(Category.BOOK);
		order.setDescrition("very interesting book");
		order.setPrice(100F);
		order.setStatus(OrderStatus.IN_PROGRESS);
		order.setVat(8F);
		
		return order;
	}
	
	
	public static <T> String getString(T object) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
	
}
