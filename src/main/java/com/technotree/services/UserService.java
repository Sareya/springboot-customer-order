package com.technotree.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.technotree.models.user.User;

public interface UserService 
{
	List<User> getAllUsers(Long adminId);
	<T> ResponseEntity<Object> createUser(User user);
	User login(String username, String password);
	User updateUserDetails(User updateUser, Long userId);
	ResponseEntity<String> deleteUser(Long userId);
	
}
