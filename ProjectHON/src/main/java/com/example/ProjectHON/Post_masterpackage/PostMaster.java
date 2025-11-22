package com.example.ProjectHON.Post_masterpackage;

import com.example.ProjectHON.Post_Report.PostReport;
import com.example.ProjectHON.Rating_masterpackage.RatingMaster;
import com.example.ProjectHON.Theme_masterpackage.ThemeMaster;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
public class PostMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserMaster user;

    @ManyToOne
    @JoinColumn(name = "themeId")
    private ThemeMaster theme;

    private byte[] photo;
    private LocalDateTime dateTime;
    private String hashtag;
    private String caption; // Post Title

    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL)
    private List<RatingMaster> ratings;

    @OneToMany(mappedBy = "postreport", cascade = CascadeType.ALL)
    private List<PostReport> reports;


    public PostMaster(Long postId, UserMaster user, ThemeMaster theme, byte[] photo, LocalDateTime dateTime, String hashtag, String caption, List<RatingMaster> ratings) {
        this.postId = postId;
        this.user = user;
        this.theme = theme;
        this.photo = photo;
        this.dateTime = dateTime;
        this.hashtag = hashtag;
        this.caption = caption;
        this.ratings = ratings;
    }

    public List<RatingMaster> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingMaster> ratings) {
        this.ratings = ratings;
    }

    public PostMaster() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    public ThemeMaster getTheme() {
        return theme;
    }

    public void setTheme(ThemeMaster theme) {
        this.theme = theme;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    // --- Average rating calculation ---
    @Transient
    public Double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) return 0.0;
        return ratings.stream()
                .mapToDouble(RatingMaster::getRating)
                .average()
                .orElse(0.0);
    }

}
