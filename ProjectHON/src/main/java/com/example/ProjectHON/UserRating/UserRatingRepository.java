package com.example.ProjectHON.UserRating;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    List<UserRating> findByUser(UserMaster user);

    @Query(value = """
    SELECT rc.type, SUM(ur.points)
    FROM user_rating ur
    JOIN rating_category rc ON ur.rating_category_id = rc.id
    WHERE ur.user_id = :userId
      AND DATE(ur.date_time) = CURRENT_DATE
    GROUP BY rc.type
""", nativeQuery = true)
    List<Object[]> findTodayPointsByCategory(@Param("userId") Long userId);
}