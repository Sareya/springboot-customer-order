package com.technotree;

import java.io.IOException;  
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technotree.models.user.User;
import com.technotree.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class InitializeDB 
{
	@Autowired
	ObjectMapper mapper;
	
	private static final String USERS_FILE = "/users.json";

	@Bean
	  CommandLineRunner init(UserRepository userRepository) 
	{
		return args -> {
				log.info("Populating users in database " + userRepository.saveAll(getUsers()));
		    };
	}
	
	private List<User> getUsers() throws JsonParseException, JsonMappingException, IOException
	{
		InputStream userFile = this.getClass().getResource(USERS_FILE).openStream();
		
		List<User> users = new ArrayList<>();
		try (InputStream in = userFile) 
		{
			users = mapper.readValue(new InputStreamReader(in), new TypeReference<List<User>>(){});
		}
		catch(NullPointerException e)
		{
			log.error("Unable to find file from resource during initialization", e);
		}
		
		return users;
	}
	
}
