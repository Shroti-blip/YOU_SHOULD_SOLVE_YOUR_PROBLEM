package com.example.ProjectHON.User_masterpackage;

import jakarta.persistence.*;

@Entity
public class ReferralMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long referralId;

    @ManyToOne
    @JoinColumn(name ="referred_from_user", nullable = false)
    private UserMaster referredFromUser;

    @ManyToOne
    @JoinColumn(name = "referred_to_user", nullable = false)
    private UserMaster referredToUser;

    private Boolean showPopup=false;

    public Long getReferralId() {
        return referralId;
    }

    public void setReferralId(Long referralId) {
        this.referralId = referralId;
    }

    public UserMaster getReferredFromUser() {
        return referredFromUser;
    }

    public void setReferredFromUser(UserMaster referredFromUser) {
        this.referredFromUser = referredFromUser;
    }

    public UserMaster getReferredToUser() {
        return referredToUser;
    }

    public void setReferredToUser(UserMaster referredToUser) {
        this.referredToUser = referredToUser;
    }

    public Boolean getShowPopup() {
        return showPopup;
    }

    public void setShowPopup(Boolean showPopup) {
        this.showPopup = showPopup;
    }
}
