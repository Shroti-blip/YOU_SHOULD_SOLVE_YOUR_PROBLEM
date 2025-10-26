package com.example.ProjectHON.User_Membership_Masterpackage;

import com.example.ProjectHON.Membership_masterpackage.MembershipMaster;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.security.PrivateKey;
import java.time.LocalDate;

@Entity
public class UserMembershipMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long usermembershipId;

 @ManyToOne
 @JoinColumn(name = "userId",nullable = false)
 private UserMaster userMaster;

 @ManyToOne
 @JoinColumn(name = "membershipId",nullable = false)
 private MembershipMaster membershipMaster;

    private LocalDate start_date;
    private double amount;
    private String payment_mode;
    private Long transcation_no;

    public UserMembershipMaster(long usermembershipId, UserMaster userMaster, MembershipMaster membershipMaster, LocalDate start_date, double amount, String payment_mode, Long transcation_no) {
        this.usermembershipId = usermembershipId;
        this.userMaster = userMaster;
        this.membershipMaster = membershipMaster;
        this.start_date = start_date;
        this.amount = amount;
        this.payment_mode = payment_mode;
        this.transcation_no = transcation_no;
    }

    public UserMembershipMaster() {
    }

    public long getUsermembershipId() {
        return usermembershipId;
    }

    public void setUsermembershipId(long usermembershipId) {
        this.usermembershipId = usermembershipId;
    }

    public UserMaster getUserMaster() {
        return userMaster;
    }

    public void setUserMaster(UserMaster userMaster) {
        this.userMaster = userMaster;
    }

    public MembershipMaster getMembershipMaster() {
        return membershipMaster;
    }

    public void setMembershipMaster(MembershipMaster membershipMaster) {
        this.membershipMaster = membershipMaster;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public Long getTranscation_no() {
        return transcation_no;
    }

    public void setTranscation_no(Long transcation_no) {
        this.transcation_no = transcation_no;
    }
}


