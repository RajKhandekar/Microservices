package com.lcwd.rating.RatingService.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_ratings")
public class Rating {

    @Id
    @Column(name = "ID")
    private String ratingId;

 //userId access from mysql workbench   
    private String userId;

//hotelId access from postgresql pg
    private String hotelId;

    private  int rating;
    private  String feedback;
}
