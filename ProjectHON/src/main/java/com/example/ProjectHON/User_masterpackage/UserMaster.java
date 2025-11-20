package com.example.ProjectHON.User_masterpackage;

import com.example.ProjectHON.Badge_masterpackage.BadgeMaster;
import com.example.ProjectHON.Post_Report.PostReport;
import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Rating_masterpackage.RatingMaster;
import com.example.ProjectHON.Whisper_masterpackage.WhisperMaster;
import com.example.ProjectHON.streakhistorypackage.StreakHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "users_master")
public class UserMaster implements UserDetails {
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<StreakHistory> streakHistory = new ArrayList<>();


    private String username;

    private String fullName;

    private String password;

    private String email;

    private double points;

    private byte[] profilePhoto;

    private String gender;

    private LocalDate dateOfBirth;

    private LocalDate joinDate;

    private Boolean status = true; //Active, Inactive

    private String contactNo;

    private String relationshipStatus; //Single, Married, Committed

    private String bio;

    private String jeoLocation;

    private Integer streakDays = 0  ;          // Number of consecutive days the user has posted
    private Integer streakWeeks = 0;         // Number of consecutive full weeks
    private Integer currentPointsPerPost = 0 ; // Current reward rate (points per post)
    private LocalDate lastPostDate;       // Date of the user's last post


    // Muskan's part
    @ManyToOne
    @JoinColumn(name = "referred_by_id") // column to store the referrer’s ID
    private UserMaster referredBy;

    //  this user referred these users
    @OneToMany(mappedBy = "referredBy", cascade = CascadeType.ALL)
    private List<UserMaster> referrals = new ArrayList<>();

    @OneToMany(mappedBy = "referredFromUser" , cascade = CascadeType.ALL)
    private List<ReferralMaster> referredFromUser;

    @OneToMany(mappedBy = "referredToUser" , cascade = CascadeType.ALL)
    private List<ReferralMaster> referredToUser;

//        for report thing  use this
//@OneToMany(mappedBy = "reporter")
//private List<PostReport> reportsMade;
//
//    @OneToMany(mappedBy = "offender")
//    private List<PostReport> reportsReceived;


    @Transient
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#^_])[A-Za-z\\d@$!%*?&#^_]{8,15}$",
            message = "Password must be 8–15 characters long and include at least 1 letter, 1 number, and 1 special character."
    )
    private String rawPassword;


    private String loginProvider = "DEFAULT";


    @Column(name = "complete_profile")
    private Boolean completeProfile = false;

    private String referralCode;

    private Boolean referredByPopup = false;


    public Boolean getReferredByPopup() {
        return referredByPopup;
    }

    public void setReferredByPopup(Boolean referredByPopup) {
        this.referredByPopup = referredByPopup;
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roleList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority>roles=roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
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



    public void setStatus(Boolean status) {
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


    public String getJeoLocation() {
        return jeoLocation;
    }

    public void setJeoLocation(String jeoLocation) {
        this.jeoLocation = jeoLocation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public List<StreakHistory> getStreakHistory() {
        return streakHistory;
    }

    public void setStreakHistory(List<StreakHistory> streakHistory) {
        this.streakHistory = streakHistory;
    }

    public Boolean getStatus() {
        return status;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public int getStreakWeeks() {
        return streakWeeks;
    }

    public void setStreakWeeks(int streakWeeks) {
        this.streakWeeks = streakWeeks;
    }

    public int getCurrentPointsPerPost() {
        return currentPointsPerPost;
    }

    public void setCurrentPointsPerPost(int currentPointsPerPost) {
        this.currentPointsPerPost = currentPointsPerPost;
    }

    public LocalDate getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(LocalDate lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public void setStreakDays(Integer streakDays) {
        this.streakDays = streakDays;
    }

    public void setStreakWeeks(Integer streakWeeks) {
        this.streakWeeks = streakWeeks;
    }

    public void setCurrentPointsPerPost(Integer currentPointsPerPost) {
        this.currentPointsPerPost = currentPointsPerPost;
    }

    public UserMaster getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(UserMaster referredBy) {
        this.referredBy = referredBy;
    }

    public List<UserMaster> getReferrals() {
        return referrals;
    }

    public void setReferrals(List<UserMaster> referrals) {
        this.referrals = referrals;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public String getLoginProvider() {
        return loginProvider;
    }

    public void setLoginProvider(String loginProvider) {
        this.loginProvider = loginProvider;
    }

    public Boolean getCompleteProfile() {
        return completeProfile;
    }

    public void setCompleteProfile(Boolean completeProfile) {
        this.completeProfile = completeProfile;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
