package com.example.ProjectHON.Badge_masterpackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
//import com.example.ProjectHON.User_masterpackage.Usermaster;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class BadgeMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;

    @ManyToMany
    @JoinTable(
            name = "user_badges", // Join table name for the sender
            joinColumns = @JoinColumn(name = "badge_id"), // Foreign key for the BadgeMaster
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key for the UserMaster (userId)
    )
    private List<UserMaster> user;

    private String badgeName;

    private byte[] badgeImage;

    public BadgeMaster(Long badgeId, List<UserMaster> user, String badgeName, byte[] badgeImage) {
        this.badgeId = badgeId;
        this.user = user;
        this.badgeName = badgeName;
        this.badgeImage = badgeImage;
    }

    public BadgeMaster() {
    }

    public Long getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }

    public List<UserMaster> getUser() {
        return user;
    }

    public void setUser(List<UserMaster> user) {
        this.user = user;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public byte[] getBadgeImage() {
        return badgeImage;
    }

    public void setBadgeImage(byte[] badgeImage) {
        this.badgeImage = badgeImage;
    }
}
