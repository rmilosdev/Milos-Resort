package com.milosdevelopmetn.service;

import com.milosdevelopmetn.dao.RoleDao;
import com.milosdevelopmetn.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleDao.findAll();

        return roles;
    }
}
