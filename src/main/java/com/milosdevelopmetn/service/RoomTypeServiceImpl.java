package com.milosdevelopmetn.service;

import com.milosdevelopmetn.dao.RoomTypeDao;
import com.milosdevelopmetn.model.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeServiceImpl implements RoomTypeService{

    @Autowired
    private RoomTypeDao roomTypeDao;

    @Override
    public RoomType findById(Long id) {
        Optional<RoomType> optionalRoomType = roomTypeDao.findById(id);
        RoomType roomType = optionalRoomType.get();

        return roomType;
    }

    @Override
    public List<RoomType> findAll() {
        return roomTypeDao.findAll();
    }

    @Override
    public void delete(RoomType roomType) {
        roomTypeDao.delete(roomType);
    }

    @Override
    public void save(RoomType roomType) {
        try{
            roomTypeDao.save(roomType);
        }catch (Exception ex){}
    }
}
