package com.example.ProjectHON.UserRating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingCategoryRepository extends JpaRepository<RatingCategory, Long> {
    RatingCategory findByType(String type);
}