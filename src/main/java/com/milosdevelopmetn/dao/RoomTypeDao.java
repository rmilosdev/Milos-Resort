package com.milosdevelopmetn.dao;

import com.milosdevelopmetn.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeDao extends JpaRepository<RoomType, Long> {
}
