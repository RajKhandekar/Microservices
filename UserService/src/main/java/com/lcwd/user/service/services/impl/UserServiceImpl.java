package com.lcwd.user.service.services.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String randomUserId=UUID.randomUUID().toString();
       user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
       return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user with given id not found in server!!:"+userId));
    //fetch rating of the above user from Rating service
    //http://localhost:8081/users/e0c5bf1e-c824-4ac9-b864-65a5b34e76b4

   //instead of localhost:8083 port number we can directly use an name of the service i.e RATINGERVICE 
      
        Rating[] ratingsOfUser=restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(),Rating[].class);
        logger.info("{} ",ratingsOfUser);

        List<Rating> ratings=Arrays.stream(ratingsOfUser).toList();

        List<Rating> ratingList=ratings.stream().map(rating->{
         //api call to hotel service to get the hotel
         //http://localhost:8082/hotels/b2efc247-c99f-46d9-b9fb-ca0272391488
        // ResponseEntity<Hotel> forEntity= restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
         
         Hotel hotel=hotelService.getHotel(rating.getHotelId());
         //logger.info("response status code: {}",forEntity.getStatusCode());

         //set the hotel to rating
         rating.setHotel(hotel);
         //return the rating
         return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }

}
