package com.milosdevelopmetn.service;

import com.milosdevelopmetn.model.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {
    void save(Hotel hotel, MultipartFile file);
    void delete(Hotel hotel);
    List<Hotel> findAll();
    Hotel findByName(String name);
    Hotel findById(Long id);
}
