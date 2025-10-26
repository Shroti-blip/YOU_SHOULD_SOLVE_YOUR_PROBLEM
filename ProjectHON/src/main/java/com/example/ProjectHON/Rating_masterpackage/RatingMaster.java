package com.example.ProjectHON.Rating_masterpackage;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
public class RatingMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rating_id;

   @ManyToOne
   @JoinColumn(name ="from_userid", nullable = false)
   private UserMaster userFrom;

    @ManyToOne
    @JoinColumn(name = "to_userid", nullable = false)
    private UserMaster userTo;

    @ManyToOne
    @JoinColumn(name = "postId")
    private PostMaster post_id;

    private Double rating;
    private LocalDate postDate;
    private LocalTime postTime;


    public RatingMaster(int rating_id, UserMaster userFrom, UserMaster userTo, PostMaster post_id, Double rating, LocalDate postDate, LocalTime postTime) {
        this.rating_id = rating_id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.post_id = post_id;
        this.rating = rating;
        this.postDate = postDate;
        this.postTime = postTime;
    }

    public RatingMaster() {
    }

    public int getRating_id() {
        return rating_id;
    }

    public void setRating_id(int rating_id) {
        this.rating_id = rating_id;
    }

    public UserMaster getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(UserMaster userFrom) {
        this.userFrom = userFrom;
    }

    public UserMaster getUserTo() {
        return userTo;
    }

    public void setUserTo(UserMaster userTo) {
        this.userTo = userTo;
    }

    public PostMaster getPost_id() {
        return post_id;
    }

    public void setPost_id(PostMaster post_id) {
        this.post_id = post_id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public LocalTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalTime postTime) {
        this.postTime = postTime;
    }
}
