package com.example.ProjectHON.User_masterpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<ReferralMaster,Long> {

    List<ReferralMaster>findByReferredFromUser(UserMaster userMaster);
}
