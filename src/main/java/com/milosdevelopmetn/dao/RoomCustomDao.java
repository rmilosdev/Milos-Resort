package com.milosdevelopmetn.dao;

import com.milosdevelopmetn.model.Reservation;
import com.milosdevelopmetn.model.Room;

public interface RoomCustomDao {
    void addReservation(Room room, Reservation reservation);
}
