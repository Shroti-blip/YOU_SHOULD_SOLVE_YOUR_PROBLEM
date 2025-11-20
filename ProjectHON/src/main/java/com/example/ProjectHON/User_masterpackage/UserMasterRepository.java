package com.example.ProjectHON.User_masterpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserMasterRepository extends JpaRepository<UserMaster,Long> {

    UserMaster findByEmailAndPassword(String email,String password);
    Optional<UserMaster> findByUsername(String username);
    Optional<UserMaster> findByEmail(String email);

//    Optional<UserMaster> findByUsername(String username);
    Optional<UserMaster> findByReferralCode(String referralCode);

    @Query("select max(u.userId) from UserMaster u")
    Long getUserMasterKey();


    long countByPointsGreaterThan(double points);

    //    long countUsersWithHigherAvgRatingInJiolocation(Double userAvgRating, String jiolocation);
    @Query("SELECT COUNT(u) FROM UserMaster u WHERE u.jeoLocation = :jeoLocation AND u.points > :userAvgRating")
    long countUsersWithHigherAvgRatingInJiolocation(@Param("userAvgRating") Double userAvgRating,
                                                    @Param("jiolocation") String jiolocation);

    List<UserMaster> findByJeoLocation(String jiolocation);
    List<UserMaster> findAll(); // typically already available from JpaRepository


    List<UserMaster> findAllByOrderByPointsDesc();
}
