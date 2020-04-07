package com.milosdevelopmetn.dao;

import com.milosdevelopmetn.model.Hotel;
import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao extends JpaRepository<Room, Long>, RoomCustomDao{
    List<Room> findAllByFree(boolean free);
}
