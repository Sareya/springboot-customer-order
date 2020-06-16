package com.technotree.customerorder.services;

import static org.junit.Assert.assertEquals; 

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
import com.technotree.exceptions.ResourceExistsException;
import com.technotree.exceptions.ResourceNotFoundException;
import com.technotree.models.user.User;
import com.technotree.repositories.UserRepository;
import com.technotree.services.UserServiceImpl;

@SpringBootTest
public class UserServiceImplTest 
{
	@InjectMocks
	UserServiceImpl service;
	@Mock
	UserRepository repository;
	
	@Before
	public void setUp() 
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getAllUsers_admin_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long adminId = 1L;
		User admin = TestUtils.getAdminUser();
		admin.setId(adminId);
		
		User customer = TestUtils.getUser();
		customer.setId(2L);
		Mockito.when(repository.findById(adminId)).thenReturn(Optional.of(admin));
		
		List<User> users = new ArrayList<>();
		users.add(admin);
		users.add(customer);
		Mockito.when(repository.findAll()).thenReturn(users);
		
		List<User> allUsers = service.getAllUsers(adminId);
		
		assertEquals(users, allUsers);
	}
	
	@Test
	public void getAllUsers_customer_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long customerId = 1L;
		User customer = TestUtils.getUser();
		Mockito.when(repository.findById(customerId)).thenReturn(Optional.of(customer));
		
		
		List<User> allUsers = service.getAllUsers(customerId);
		assertEquals(Collections.EMPTY_LIST, allUsers);
	}
	
	@Test(expected= ResourceNotFoundException.class)
	public void getAllUsers_exception_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long customerId = 1L;
		Mockito.when(repository.findById(customerId)).thenThrow(ResourceNotFoundException.class);
		List<User> allUsers = service.getAllUsers(customerId);
	}
	
	@Test
	public void createUser_test() throws JsonParseException, JsonMappingException, IOException
	{
		Long adminId = 1L;
		User admin = TestUtils.getAdminUser();
		admin.setId(adminId);
		
		Long cutomerId = 2L;
		User customer = TestUtils.getUser();
		customer.setId(cutomerId);
		
		User admin2 = TestUtils.getAdminUser();
		admin2.setUsername("adminTest2");
		List<User> users = new ArrayList<>();
		users.add(admin);
		users.add(customer);
		
		Mockito.when(repository.findAll()).thenReturn(users);
		Mockito.when(repository.save(admin2)).thenReturn(admin2);
		
		ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("User created ");
		
		ResponseEntity<Object> actualResponse = service.createUser(admin2);
		assertEquals(response, actualResponse);
	}
	
	@Test
	public void createUser_alreadyExists_test() throws JsonParseException, JsonMappingException, IOException 
	{
		Long adminId = 1L;
		User admin = TestUtils.getAdminUser();
		admin.setId(adminId);
		
		Long cutomerId = 2L;
		User customer = TestUtils.getUser();
		customer.setId(cutomerId);
		
		List<User> users = new ArrayList<>();
		users.add(admin);
		users.add(customer);
		
		Mockito.when(repository.findAll()).thenReturn(users);
		
		ResponseEntity<String> response = ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(new ResourceExistsException("Username" , customer.getUsername()).getMessage());
		
		ResponseEntity<Object> actualResponse = service.createUser(customer);
		assertEquals(response, actualResponse);
	}
	
	@Test
	public void login_test() throws JsonParseException, JsonMappingException, IOException
	{
		String username = "testUsername";
		String password = "testPassword";
		User user = TestUtils.getUser();
		user.setId(1L);
		
		Mockito.when(repository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(user));
		
		User result = service.login(username, password);
		
		assertEquals(user, result);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void login_exception_test() throws JsonParseException, JsonMappingException, IOException
	{
		String username = "testUsername";
		String password = "testPassword";
		
		Mockito.when(repository.findByUsernameAndPassword(username, password)).thenThrow(ResourceNotFoundException.class);
		
		User result = service.login(username, password);
	}
	
	@Test
	public void updateUser_test() throws JsonParseException, JsonMappingException, IOException 
	{
		Long userId = 1L;
		User user = TestUtils.getUser();
		user.setId(userId);
		
		User updateUser = TestUtils.getUser();
		updateUser.setPassword("newDummyPassword");
		
		user.setPassword(updateUser.getPassword());
		
		Mockito.when(repository.findById(userId)).thenReturn(Optional.of(user));
		user.setPassword(updateUser.getPassword());
		Mockito.when(repository.save(user)).thenReturn(user);
		
		User actualResponse = service.updateUserDetails(updateUser, userId);
		assertEquals(user, actualResponse);
	}
	
	@Test
	public void deleteUser_test() throws JsonParseException, JsonMappingException, IOException 
	{
		Long userId = 1L;
		
		Mockito.doNothing().when(repository).deleteById(Mockito.isA(Long.class)); 
		ResponseEntity<String> expectedResponse = ResponseEntity.ok("User " + userId + " has been deleted");
		ResponseEntity<String> actualResponse = service.deleteUser(userId);
		
		assertEquals(expectedResponse, actualResponse);
	}
	
	@Test
	public void deleteUser_exception_test() throws JsonParseException, JsonMappingException, IOException 
	{
		Long userId = 1L;
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(Mockito.isA(Long.class));
		ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user " + userId + " found to be deleted");
		ResponseEntity<String> actualResponse = service.deleteUser(userId);
		
		assertEquals(response, actualResponse);
	}
}
