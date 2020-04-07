package com.milosdevelopmetn.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.milosdevelopmetn.model.Reservation;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Long> {
}
