package com.milosdevelopmetn.service;

import com.milosdevelopmetn.dao.ReservationDao;
import com.milosdevelopmetn.dao.RoomDao;
import com.milosdevelopmetn.model.Hotel;
import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Room;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private ReservationDao reservationDao;

    @Override
    public Room findById(Long id) {
        Optional<Room> optionalRoom = roomDao.findById(id);
        Room room = optionalRoom.get();

        return room;
    }

    @Override
    public List<Room> findAll() {
        return roomDao.findAll();
    }

    @Override
    public List<Room> findAllByFree(boolean free)
    {
       return roomDao.findAllByFree(free);
    }

    @Override
    public void delete(Room room) {
        roomDao.delete(room);
    }

    @Override
    public void save(Room room) {
        roomDao.save(room);
    }

    @Override
    public void addReservation(Room room, Long reservationId) {
        Optional<Reservation>reservationOptional = reservationDao.findById(reservationId);
        Reservation reservation = reservationOptional.get();

        room.setReservation(reservation);
        room.setFree(true);

        roomDao.save(room);
    }
}
