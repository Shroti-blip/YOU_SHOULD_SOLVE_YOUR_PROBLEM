package com.example.ProjectHON.UserRating;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class UserRatingController {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private RatingCategoryRepository ratingCategoryRepository;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @PostMapping("/save/{userId}")
    public String saveUserRating(@PathVariable Long userId,
                                 @RequestParam String categoryType,
                                 @RequestParam Integer points,
                                 Model model) {

        Optional<UserMaster> userOpt = userMasterRepository.findById(userId);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "error";
        }

        RatingCategory category = ratingCategoryRepository.findByType(categoryType);
        if (category == null) {
            category = new RatingCategory();
            category.setType(categoryType);
            ratingCategoryRepository.save(category);
        }

        UserRating userRating = new UserRating();
        userRating.setUser(userOpt.get());
        userRating.setRatingCategory(category);
        userRating.setPoints(points);
        userRating.setDateTime(LocalDateTime.now());

        userRatingRepository.save(userRating);

        model.addAttribute("message", "User rating saved successfully");
        return "redirect:/dashboard/" + userId;
    }
}