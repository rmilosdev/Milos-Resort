package com.milosdevelopmetn.service;

import com.milosdevelopmetn.dao.RoleDao;
import com.milosdevelopmetn.model.Role;
import com.milosdevelopmetn.model.User;
import com.milosdevelopmetn.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        Optional<User> optionalUser = userDao.findById(id);
        User user = optionalUser.get();

        return user;
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }



    @Override
    public void saveUser(User user) {
        if(user.getId() != null){
            if(userDao.existsById(user.getId())){
                Role role = roleDao.findByName("ROLE_USER");
                user.setRole(role);
                userDao.save(user);
            }
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            Role role = roleDao.findByName("ROLE_USER");
            user.setRole(role);
            userDao.save(user);
        }

    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void updatePassword(User user) {
        if(user.getId() != null){
            if(userDao.existsById(user.getId())){
                user.setPassword(encoder.encode(user.getPassword()));
                Role role = roleDao.findByName("ROLE_USER");
                user.setRole(role);
                userDao.save(user);
            }
        }
    }

    @Override
    public void deleteUser(User user) {
        userDao.delete(user);
    }

}
