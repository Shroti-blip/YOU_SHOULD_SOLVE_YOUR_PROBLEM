package com.example.ProjectHON.Post_masterpackage;

import com.example.ProjectHON.Theme_masterpackage.ThemeMaster;
import com.example.ProjectHON.User_masterpackage.UserMaster;
//import com.example.ProjectHON.User_masterpackage.Usermaster;
import jakarta.persistence.*;


import java.time.LocalDateTime;

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

    private Long rating;

    private LocalDateTime dateTime;

    private String hashtag;

    private String caption;//Post Title

    public PostMaster(Long postId, UserMaster user, ThemeMaster theme, byte[] photo, Long rating, LocalDateTime dateTime, String hashtag, String caption) {
        this.postId = postId;
        this.user = user;
        this.theme = theme;
        this.photo = photo;
        this.rating = rating;
        this.dateTime = dateTime;
        this.hashtag = hashtag;
        this.caption = caption;
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

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
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
}
