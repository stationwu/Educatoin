package com.edu.dao;

import org.springframework.data.repository.CrudRepository;

import com.edu.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{

}
