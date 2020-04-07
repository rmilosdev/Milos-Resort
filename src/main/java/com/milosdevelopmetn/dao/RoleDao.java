package com.milosdevelopmetn.dao;

import com.milosdevelopmetn.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
     Role findByName(String role);
}
