package com.technotree.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.technotree.models.user.User;

public interface UserRepository extends CrudRepository<User, Long>
{
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndPassword(String username, String password);
}
