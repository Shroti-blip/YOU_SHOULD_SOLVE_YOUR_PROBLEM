package com.example.ProjectHON.User_masterpackage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMasterRepository extends JpaRepository<UserMaster,Long> {
    Optional<UserMaster> findByEmail(String email);
    Optional<UserMaster> findByEmailAndPassword(String email , String password);
    Optional<UserMaster> findByUsername(String username);
    Optional<UserMaster> findByReferralCode(String referralCode);

}
