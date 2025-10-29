package com.example.ProjectHON.User_masterpackage;

import com.example.ProjectHON.Badge_masterpackage.BadgeMaster;
import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Rating_masterpackage.RatingMaster;
import com.example.ProjectHON.Whisper_masterpackage.WhisperMaster;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "users_master")
public class UserMaster{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BadgeMaster> badge;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostMaster> post;

    @OneToMany(mappedBy = "userFrom" , cascade = CascadeType.ALL)
    private List<RatingMaster> userFrom;

    @OneToMany(mappedBy = "userTo" , cascade = CascadeType.ALL)
    private List<RatingMaster> userTo;

    @ManyToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<WhisperMaster> sentWhispers; // Sender

    @ManyToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<WhisperMaster> receivedWhispers; // Receiver

//    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be 4–20 characters long")
    private String username;

//    @Pattern(
//            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,15}$",
//            message = "Password must be 6–15 characters long and include at least 1 letter and 1 number"
//    )
//@Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
//@Pattern(
//        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#^_])[A-Za-z\\d@$!%*?&#^_]{6,15}$",
//        message = "Password must be 6–15 characters long and include at least 1 letter, 1 number, and 1 special character."
//)
// 🟩 Add a transient field for validation
@Transient
@Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#^_])[A-Za-z\\d@$!%*?&#^_]{6,15}$",
        message = "Password must be 6–15 characters long and include at least 1 letter, 1 number, and 1 special character."
)
private String rawPassword;

private String password;

    @Email(message = "Please enter a valid email address")
    private String email;

    private double points;

    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    private String gender;

    private LocalDate dateOfBirth;

    private LocalDate joinDate;

    private String status; //Active, Inactive

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit Indian mobile number"
    )
    private String contactNo;

    private String relationshipStatus; //Single, Married, Committed

    private String bio;

    private String jiolocation;

    public UserMaster() {
    }

    public UserMaster(Long userId, List<BadgeMaster> badge, List<PostMaster> post, List<RatingMaster> userFrom, List<RatingMaster> userTo, List<WhisperMaster> sentWhispers, List<WhisperMaster> receivedWhispers, String username, String password, String email, double points, byte[] profilePhoto, String gender, LocalDate dateOfBirth, LocalDate joinDate, String status, String contactNo, String relationshipStatus, String bio, String jiolocation) {
        this.userId = userId;
        this.badge = badge;
        this.post = post;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.sentWhispers = sentWhispers;
        this.receivedWhispers = receivedWhispers;
        this.username = username;
        this.password = password;
        this.email = email;
        this.points = points;
        this.profilePhoto = profilePhoto;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.joinDate = joinDate;
        this.status = status;
        this.contactNo = contactNo;
        this.relationshipStatus = relationshipStatus;
        this.bio = bio;
        this.jiolocation = jiolocation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<BadgeMaster> getBadge() {
        return badge;
    }

    public void setBadge(List<BadgeMaster> badge) {
        this.badge = badge;
    }

    public List<PostMaster> getPost() {
        return post;
    }

    public void setPost(List<PostMaster> post) {
        this.post = post;
    }

    public List<RatingMaster> getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(List<RatingMaster> userFrom) {
        this.userFrom = userFrom;
    }

    public List<RatingMaster> getUserTo() {
        return userTo;
    }

    public void setUserTo(List<RatingMaster> userTo) {
        this.userTo = userTo;
    }

    public List<WhisperMaster> getSentWhispers() {
        return sentWhispers;
    }

    public void setSentWhispers(List<WhisperMaster> sentWhispers) {
        this.sentWhispers = sentWhispers;
    }

    public List<WhisperMaster> getReceivedWhispers() {
        return receivedWhispers;
    }

    public void setReceivedWhispers(List<WhisperMaster> receivedWhispers) {
        this.receivedWhispers = receivedWhispers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getJiolocation() {
        return jiolocation;
    }

    public void setJiolocation(String jiolocation) {
        this.jiolocation = jiolocation;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }
}
