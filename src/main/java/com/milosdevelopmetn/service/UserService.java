package com.milosdevelopmetn.service;

import com.milosdevelopmetn.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);
    User findById(Long id);
    List<User> findAll();
    void saveUser(User user);
    void save(User user);
    void updatePassword(User user);
    void deleteUser(User user);
}
