package com.example.ProjectHON.Membership_masterpackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class MembershipMaster {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;


    private String name;

    private double amount;

    public MembershipMaster(Long membershipId, String name, double amount) {
        this.membershipId = membershipId;
        this.name = name;
        this.amount = amount;
    }

    public MembershipMaster() {
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
