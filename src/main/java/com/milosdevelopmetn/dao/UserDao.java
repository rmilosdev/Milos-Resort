package com.milosdevelopmetn.dao;

import com.milosdevelopmetn.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User,Long> {

    User findByUsername(String username);
}
