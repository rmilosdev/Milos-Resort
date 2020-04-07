package com.milosdevelopmetn.service;

import com.milosdevelopmetn.dao.ReservationDao;
import com.milosdevelopmetn.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDao reservationDao;


    @Override
    public Reservation findById(Long id) {
        Optional<Reservation> optionalReservation = reservationDao.findById(id);
        Reservation reservation = optionalReservation.get();

        return  reservation;
    }

    @Override
    public List<Reservation> findAll() {
//        return reservationDao.findAll();
        return (List)reservationDao.findAll();
    }

    @Override
    public void delete(Reservation reservation) {
        reservationDao.delete(reservation);
    }

    @Override
    public void deleteById(Long id){
        reservationDao.deleteById(id);
    }

    @Override
    public void save(Reservation reservation) {
        reservationDao.save(reservation);
    }


}
