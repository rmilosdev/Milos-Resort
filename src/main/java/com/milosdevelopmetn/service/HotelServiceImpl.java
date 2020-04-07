package com.milosdevelopmetn.service;

import com.milosdevelopmetn.dao.HotelDao;
import com.milosdevelopmetn.model.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService{
    @Autowired
    private HotelDao hotelDao;

    @Override
    public void save(Hotel hotel, MultipartFile file) {
        try {
            hotel.setBytes(file.getBytes());
            hotelDao.save(hotel);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Hotel hotel) {
        hotelDao.delete(hotel);
    }

    @Override
    public List<Hotel> findAll() {
        return hotelDao.findAll();
    }

    @Override
    public Hotel findByName(String name) {
        return hotelDao.findByName(name);
    }


    @Override
    public Hotel findById(Long id) {

        Optional<Hotel> optionalHotel = hotelDao.findById(id);
        Hotel hotel = optionalHotel.get();

        return hotel;
    }
}
