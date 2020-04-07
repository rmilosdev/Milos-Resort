package com.milosdevelopmetn.service;

import com.milosdevelopmetn.model.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation findById(Long id);
    List<Reservation> findAll();
    void delete(Reservation reservation);
    void deleteById(Long id);
    void save(Reservation reservation);
}
