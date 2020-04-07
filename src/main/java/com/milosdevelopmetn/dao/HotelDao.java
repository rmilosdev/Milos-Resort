package com.milosdevelopmetn.dao;

import com.milosdevelopmetn.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelDao  extends JpaRepository<Hotel, Long> {
    Hotel findByName(String name);
}
