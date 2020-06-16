package com.technotree.services;

import java.util.Collections; 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.technotree.exceptions.ResourceExistsException;
import com.technotree.exceptions.ResourceNotFoundException;
import com.technotree.models.user.Role;
import com.technotree.models.user.User;
import com.technotree.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService
{
	private static final Logger LOG = LoggerFactory.getLogger( UserService.class );
	private static final String ADMIN_SECRET_KEY= "verysecretkey";
	
	@Autowired
	UserRepository repository;
	
	@Override
	public List<User> getAllUsers(Long adminId)
	{
		return isAdmin(adminId)? 
				(List<User>) repository.findAll() :
					Collections.emptyList();
	}
	
	@Override
	public  <T> ResponseEntity<Object> createUser(User user)
	{
		String username = user.getUsername();
		LOG.info("--- Fetching all users ---");
		Iterable<User> users = repository.findAll();
		
		if (usernameExists( username, users))
		{
			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body(new ResourceExistsException("Username" , username).getMessage());
		}
		
		if (user.getAdminSecretKey().equals(ADMIN_SECRET_KEY))
		{
			LOG.info("Setting user {} role to admin", username);
			user.setRole(Role.ADMIN);
		}
			
		LOG.debug("Saving {} data to DB", username);
		repository.save(user);
		LOG.info("User with username {} created", username);
	    return ResponseEntity
	    		.status(HttpStatus.CREATED)
	    		.body("User created ");
	}
	
	@Override
	public User login(String username, String password)
	{
		return  repository.findByUsernameAndPassword(username, password)
				.orElseThrow( () -> new ResourceNotFoundException("Invalid username or password") );
	}
	
	@Override
	public User updateUserDetails(User updateUser, Long userId)
	{
		LOG.debug("Updating user {} details", userId);
	    return repository.findById(userId)
	      .map( user -> {
	    	  user.setPassword(updateUser.getPassword());
	    	  user.setGender(updateUser.getGender());
	    	  user.setFullname(updateUser.getFullname());
	    	  user.setAddress(updateUser.getAddress());
	    	  user.setPhone(updateUser.getPhone());
	    	  LOG.debug("Saving user {} details", userId);
	    	  return repository.save(user);
	      })
	      .orElseGet(() -> {
	    	  updateUser.setId(userId);
	        return repository.save(updateUser);
	      });
	 }
	
	@Override
	public ResponseEntity<String> deleteUser(Long userId) 
	{
		try
		{
			LOG.warn("Deleting user with id {}", userId);
			repository.deleteById(userId);
			LOG.info("User {} deleted", userId);
		}
		catch(EmptyResultDataAccessException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user " + userId + " found to be deleted");
		}

		return ResponseEntity.ok("User " + userId + " has been deleted");
	}
	
	public User getUserById(Long id)
	{
		return repository.findById(id)
				.orElseThrow( () -> new ResourceNotFoundException("user", id) );
	}
	
	public Boolean isAdmin(Long userId)
	{
		User user = getUserById(userId);
		LOG.info("User {} is {}", userId, user.getRole());
		return user.getRole().equals(Role.ADMIN) ? true : false;
	}
	
	private boolean usernameExists(String username, Iterable<User> users)
	{
		for(User user : users)
		{
			if( user.getUsername().equals(username))
			{
				LOG.warn("Username {} already exists for user with id {}", username, user.getId());
				return true;
			}
				
		}
		
		return false;
	}
	
	
}
