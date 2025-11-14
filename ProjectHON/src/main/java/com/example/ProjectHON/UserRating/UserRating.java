package com.example.ProjectHON.UserRating;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_rating")
public class UserRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserMaster user;

    @ManyToOne
    @JoinColumn(name = "rating_category_id", nullable = false)
    private RatingCategory ratingCategory;

    @Column(nullable = false)
    private Integer points;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    public RatingCategory getRatingCategory() {
        return ratingCategory;
    }

    public void setRatingCategory(RatingCategory ratingCategory) {
        this.ratingCategory = ratingCategory;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}