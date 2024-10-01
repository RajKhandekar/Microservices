package com.lcwd.rating.RatingService.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.rating.RatingService.entities.Rating;

public interface RatingRepository extends JpaRepository<Rating,String> {
    
//sometime our own methods we use 
     //custom finder methods
     List<Rating> findByUserId(String userId);
     List<Rating> findByHotelId(String hotelId);
}
