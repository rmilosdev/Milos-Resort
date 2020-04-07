package com.milosdevelopmetn.service;

import com.milosdevelopmetn.model.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomTypeService {
    RoomType findById(Long id);
    List<RoomType> findAll();
    void delete(RoomType roomType);
    void save(RoomType roomType);
}
