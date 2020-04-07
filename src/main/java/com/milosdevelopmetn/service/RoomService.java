package com.milosdevelopmetn.service;

import com.milosdevelopmetn.model.Hotel;
import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Predicate;

public interface RoomService {
    Room findById(Long id);
    List<Room> findAll();
    List<Room> findAllByFree(boolean free);
    void delete(Room room);
    void save(Room room);
    void addReservation(Room room, Long id);
}
