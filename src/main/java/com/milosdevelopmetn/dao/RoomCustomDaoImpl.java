package com.milosdevelopmetn.dao;

import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RoomCustomDaoImpl implements RoomCustomDao {
    @Override
    public void addReservation(Room room, Reservation reservation) {
        room.setReservation(reservation);
    }
}
