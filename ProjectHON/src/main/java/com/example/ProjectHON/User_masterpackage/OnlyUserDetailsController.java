package com.example.ProjectHON.User_masterpackage;

import com.example.ProjectHON.Badge_masterpackage.BadgeMaster;
import com.example.ProjectHON.Badge_masterpackage.BadgeMasterRepository;
import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Rating_masterpackage.RatingMaster;
import com.example.ProjectHON.Rating_masterpackage.RatingRepository;
import com.example.ProjectHON.UserRating.UserRatingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class OnlyUserDetailsController {

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    ReferralRepository referralRepository;

    @GetMapping("/rating")
    public String getUserDashboard(Model model, HttpSession session){

        UserMaster userMaster = (UserMaster) session.getAttribute("user_master");
        System.out.println("Inside the User Dashboard Method" +userMaster.getEmail());
        if (userMaster == null) {
            return "redirect:/login"; // if session expired
        }


        //  Get all post IDs already rated by this user
        List<Integer> ratedPostIds = ratingRepository.findRatedPostIdsByUser(userMaster.getUserId());

        // Fetch posts that are NOT rated yet
        List<PostMaster> unRatedPosts;

        if (ratedPostIds.isEmpty()) {
            unRatedPosts = postRepository.findAll(); // if no ratings yet
        } else {
            unRatedPosts = postRepository.findByPostIdNotIn(ratedPostIds);
        }

        List<PostMaster> postMasterList=postRepository.findByUser(userMaster);

        Double sumAllPostRating=0.0;
        int count=0;
        for(PostMaster postMaster : postMasterList){
            Double rating=0.0;
            for(RatingMaster ratingMaster : postMaster.getRatings()) {
                rating += ratingMaster.getRating();
                count++;
            }
            sumAllPostRating +=rating;
        }

        // Pass data to frontend
        model.addAttribute("posts", unRatedPosts);
        model.addAttribute("allPostRatingAverage",String.format("%.2f", sumAllPostRating/count));
        model.addAttribute("sumAllPostRating",String.format("%.2f", sumAllPostRating));
        model.addAttribute("own_user_posts", postMasterList);
        model.addAttribute("user_master", userMaster);
//        model.addAttribute("popupPoints", "You just earned 50 points for registering with a referral code!");// "🎉 You signed up successfully using a referral code! You and your referrer earned 100 points!");


        int referralPoints = 0;
        int counter=0;
         List<ReferralMaster>referralMasterList=referralRepository.findByReferredFromUser(userMaster);
        System.out.println("Referral Count is "+referralMasterList.size());
        for(ReferralMaster referral : referralMasterList){
            if(!referral.getShowPopup()) {
                referralPoints += 150;
                counter++;
                referral.setShowPopup(true);
                referralRepository.save(referral);
            }
        }
        System.out.println("Counter "+counter);

        System.out.println("Total point made "+referralPoints);
        if(!referralMasterList.isEmpty() && counter>0){
            model.addAttribute("referralMessage","You earned "+referralPoints+" points! A total of "+counter+" users have joined through your referral.");
            model.addAttribute("referralPoint",referralPoints);

            System.out.println("Inside the if");
        }


        return "rating";
    }



    @Autowired
    UserMasterRepository userRepository;

    @Autowired
    BadgeMasterRepository badgeRepository;

    @Autowired
    UserRatingRepository userRatingRepository;


    @GetMapping(value = {"/dashboard", "/dashboard/{userId}"})
    public String getDashboard(@PathVariable(required = false) Long userId, Model model, HttpSession session) {

       UserMaster sessionUser = (UserMaster) session.getAttribute("user_master");
        session.setAttribute("userId", sessionUser.getUserId());

        if (userId == null) {
            userId = sessionUser.getUserId();
        }
        System.out.println("User Id is "+userId);

        Optional<UserMaster> optDashboardUser = userRepository.findById(userId);
        if (optDashboardUser.isEmpty()) {
            model.addAttribute("error", "Dashboard user not found");
            return "error";
        }

        UserMaster user = optDashboardUser.get();




        boolean isOwnDashboard = sessionUser.getUserId().equals(user.getUserId());
        model.addAttribute("isOwnDashboard", isOwnDashboard);
        String profileImageDataUrl = "";
        if (user.getProfilePhoto() != null && user.getProfilePhoto().length > 0) {
            String base64 = Base64.getEncoder().encodeToString(user.getProfilePhoto());
            profileImageDataUrl = "data:image/jpeg;base64," + base64;
        }

        Integer highestRating = ratingRepository.findHighestPostRatingForUser(user.getUserId());
        if (highestRating == null) highestRating = 0;

        Map<Long, Double> userTotals = new HashMap<>();
        List<UserMaster> allUsers = userRepository.findAll();
        for (UserMaster u : allUsers) {
            userTotals.put(u.getUserId(), 0.0);
        }

        List<Object[]> totals = ratingRepository.findUserTotalPoints();
        if (totals != null) {
            for (Object[] row : totals) {
                if (row == null || row.length < 2) continue;
                Number uidNum = (Number) row[0];
                Number sumNum = (Number) row[1];
                if (uidNum == null) continue;
                Long uid = uidNum.longValue();
                Double sum = sumNum != null ? sumNum.doubleValue() : 0.0;
                userTotals.put(uid, sum);
            }
        }

        List<Map.Entry<Long, Double>> globalSorted = userTotals.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());

        long globalRank = 0;
        for (int i = 0; i < globalSorted.size(); i++) {
            if (Objects.equals(globalSorted.get(i).getKey(), userId)) {
                globalRank = i + 1L;
                break;
            }
        }
        if (globalRank == 0) globalRank = globalSorted.size() + 1L;

        int globalAheadPercent = 0;
        if (!globalSorted.isEmpty()) {
            globalAheadPercent = ((int) (globalSorted.size() - globalRank + 1) / globalSorted.size()) * 100;
            if (globalAheadPercent < 15) {
                globalAheadPercent = 20;
            }
        }

        String jiolocation = user.getJeoLocation();
        long localRank = 0L;
        int localAheadPercent = 0;

        if (jiolocation != null && !jiolocation.trim().isEmpty()) {
            List<UserMaster> localUsers = userRepository.findByJeoLocation(jiolocation);
            Set<Long> localIds = localUsers.stream().map(UserMaster::getUserId).collect(Collectors.toSet());

            List<Map.Entry<Long, Double>> localSorted = globalSorted.stream()
                    .filter(e -> localIds.contains(e.getKey()))
                    .collect(Collectors.toList());

            for (int i = 0; i < localSorted.size(); i++) {
                if (Objects.equals(localSorted.get(i).getKey(), userId)) {
                    localRank = i + 1L;
                    break;
                }
            }

            if (localRank == 0 && localIds.contains(userId)) {
                localRank = localSorted.size() + 1L;
            }

            if (!localSorted.isEmpty()) {
                localAheadPercent = ((int) (localSorted.size() - localRank + 1) / localSorted.size()) * 100;
                if (localAheadPercent < 15) {
                    localAheadPercent = 20;
                }
            }
        }

        Double totalRatingDouble = ratingRepository.sumRatingsForUser(user.getUserId());
        if (totalRatingDouble == null) totalRatingDouble = 0.0;
        Long totalRating = totalRatingDouble.longValue();

        String badgeName = null;
        if (totalRatingDouble <= 100.0) {
            badgeName = "Silver";
        } else if (totalRatingDouble > 100.0 && totalRatingDouble <= 500.0) {
            badgeName = "Gold";
        }

        String badgeImageBase64 = null;
        if (badgeName != null) {
            Optional<BadgeMaster> badgeOpt = badgeRepository.findByBadgeName(badgeName);
            if (badgeOpt.isPresent()) {
                BadgeMaster badge = badgeOpt.get();
                if (badge.getBadgeImage() != null && badge.getBadgeImage().length > 0) {
                    String b64 = Base64.getEncoder().encodeToString(badge.getBadgeImage());
                    badgeImageBase64 = "data:image/png;base64," + b64;
                }
            }
        }

        List<PostMaster> posts = postRepository.findByUserOrderByDateTimeDesc(user);
        LocalDateTime now = LocalDateTime.now();

        List<Map<String, Object>> postDtos = posts.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("postId", p.getPostId());
            m.put("caption", p.getCaption());
            m.put("hashtag", p.getHashtag());

            if (p.getDateTime() != null) {
                LocalDateTime postDateTime = p.getDateTime();
                Duration duration = Duration.between(postDateTime, now);

                long totalMinutes = duration.toMinutes();
                long totalHours = duration.toHours();
                long totalDays = duration.toDays();

                String displayDate;
                if (totalDays >= 365) {
                    long years = totalDays / 365;
                    displayDate = years == 1 ? "1 year ago" : years + " years ago";
                } else if (totalDays >= 30) {
                    long months = totalDays / 30;
                    displayDate = months == 1 ? "1 month ago" : months + " months ago";
                } else if (totalDays >= 7) {
                    long weeks = totalDays / 7;
                    displayDate = weeks == 1 ? "1 week ago" : weeks + " weeks ago";
                } else if (totalDays >= 1) {
                    displayDate = totalDays == 1 ? "1 day ago" : totalDays + " days ago";
                } else if (totalHours >= 1) {
                    displayDate = totalHours == 1 ? "1 hour ago" : totalHours + " hours ago";
                } else if (totalMinutes >= 1) {
                    displayDate = totalMinutes == 1 ? "1 minute ago" : totalMinutes + " minutes ago";
                } else {
                    displayDate = "Just now";
                }

                m.put("dateTime", displayDate);
            } else {
                m.put("dateTime", "N/A");
            }

            Double totalPostRating = ratingRepository.sumRatingsForPost(p.getPostId());
            Long totalVotes = ratingRepository.countRatingsForPost(p.getPostId());
            if (totalPostRating == null) totalPostRating = 0.0;
            if (totalVotes == null || totalVotes == 0) totalVotes = 1L;
            Double avgPostRating = totalPostRating / totalVotes;

            m.put("avgRating", avgPostRating);

            if (p.getPhoto() != null && p.getPhoto().length > 0) {
                String pBase64 = Base64.getEncoder().encodeToString(p.getPhoto());
                m.put("photoDataUrl", "data:image/jpeg;base64," + pBase64);
            } else {
                m.put("photoDataUrl", null);
            }
            return m;
        }).collect(Collectors.toList());

        String analyticsLine1 = String.format("Hello %s, You joined us on %s",
                user.getUsername(),
                user.getJoinDate() != null ? user.getJoinDate().toString() : "N/A");

        String analyticsLine2 = String.format("You have posted %d Photos and Among them the highest rating you got is %d.",
                posts.size(), highestRating);

        String analyticsLine4 = String.format("You are ahead of %d%% of users globally.", globalAheadPercent);
        String analyticsLine5 = jiolocation != null && !jiolocation.trim().isEmpty()
                ? String.format("You are ahead of %d%% of users locally in %s.", localAheadPercent, jiolocation)
                : "Local ranking data unavailable.";

        List<UserMaster> users = userRepository.findAllByOrderByPointsDesc();
        Integer userid = ((Long) session.getAttribute("userId")).intValue();
        Optional<UserMaster> loggedUser = userRepository.findById(userid.longValue());
        model.addAttribute("whereami", loggedUser.map(UserMaster::getUsername).orElse("Unknown"));
        model.addAttribute("users", users);

        UserMaster aboveUser = null;
        UserMaster belowUser = null;
        String AboveName = null;
        String BelowName = null;

        int currentIndex = -1;
        for (int i = 0; i < globalSorted.size(); i++) {
            if (Objects.equals(globalSorted.get(i).getKey(), userId)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex != -1) {
            if (currentIndex > 0) {
                Long aboveUserId = globalSorted.get(currentIndex - 1).getKey();
                aboveUser = userRepository.findById(aboveUserId).orElse(null);
                if (aboveUser != null) {
                    AboveName = aboveUser.getUsername();
                }
            }
            if (currentIndex < globalSorted.size() - 1) {
                Long belowUserId = globalSorted.get(currentIndex + 1).getKey();
                belowUser = userRepository.findById(belowUserId).orElse(null);
                if (belowUser != null) {
                    BelowName = belowUser.getUsername();
                }
            }
        }
        String aboveUserImage = null;
        String belowUserImage = null;
        String currentUserImage = null;

        if (aboveUser != null && aboveUser.getProfilePhoto() != null && aboveUser.getProfilePhoto().length > 0) {
            aboveUserImage = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(aboveUser.getProfilePhoto());
        }
        if (belowUser != null && belowUser.getProfilePhoto() != null && belowUser.getProfilePhoto().length > 0) {
            belowUserImage = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(belowUser.getProfilePhoto());
        }
        if (user.getProfilePhoto() != null && user.getProfilePhoto().length > 0) {
            currentUserImage = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getProfilePhoto());
        }

        List<Object[]> topAdmirersData = ratingRepository.findTopAdmirersForUser(userId);
        List<Map<String, Object>> topAdmirers = new ArrayList<>();

        int maxAdmirers = Math.min(3, topAdmirersData.size());
        for (int i = 0; i < maxAdmirers; i++) {
            Object[] row = topAdmirersData.get(i);
            Long admirerId = ((Number) row[0]).longValue();

            Optional<UserMaster> admirerOpt = userRepository.findById(admirerId);
            admirerOpt.ifPresent(admirer -> {
                Map<String, Object> admirerMap = new HashMap<>();
                admirerMap.put("id", admirer.getUserId());
                admirerMap.put("name", admirer.getUsername());

                byte[] photoData = admirer.getProfilePhoto(); // assuming column is byte[]
                if (photoData != null && photoData.length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(photoData);
                    admirerMap.put("photo", "data:image/jpeg;base64," + base64Image);
                } else {
                    admirerMap.put("photo", "/images/default-avatar.jpg"); // fallback
                }
                topAdmirers.add(admirerMap);
            });
        }

        List<Object[]> todayPointsData = userRatingRepository.findTodayPointsByCategory(userId);

        Map<String, Integer> categoryPointsMap = new LinkedHashMap<>();
        boolean hasValidPoints = true;
        for (Object[] row : todayPointsData) {
            String categoryType = (String) row[0];
            Integer totalPoints = row[1] != null ? ((Number) row[1]).intValue() : null;
            if (totalPoints == null) {
                hasValidPoints = false;
            }
            categoryPointsMap.put(categoryType, totalPoints);
        }

        model.addAttribute("categoryPointsMap", categoryPointsMap);
        model.addAttribute("hasValidPoints", hasValidPoints);
        model.addAttribute("isOwnDashboard", isOwnDashboard);
        model.addAttribute("topAdmirers", topAdmirers);
        model.addAttribute("aboveUserImage", aboveUserImage);
        model.addAttribute("belowUserImage", belowUserImage);
        model.addAttribute("currentUserImage", currentUserImage);
        model.addAttribute("aboveUser", aboveUser);
        model.addAttribute("AboveName", AboveName);
        model.addAttribute("belowUser", belowUser);
        model.addAttribute("BelowName", BelowName);
        model.addAttribute("user", user);
//        model.addAttribute("username", username);
        model.addAttribute("profileImage", profileImageDataUrl);
        model.addAttribute("badgeName", badgeName);
        model.addAttribute("badgeImage", badgeImageBase64);
        model.addAttribute("highestRating", highestRating);
        model.addAttribute("globalRank", globalRank);
        model.addAttribute("localRank", localRank);
        model.addAttribute("postDtos", postDtos);
        model.addAttribute("totalRating", totalRating);
        model.addAttribute("analyticsLine1", analyticsLine1);
        model.addAttribute("analyticsLine2", analyticsLine2);
        model.addAttribute("analyticsLine4", analyticsLine4);
        model.addAttribute("analyticsLine5", analyticsLine5);

        return "user-dashboard";
    }


    @GetMapping("/profile2")
    public String getProfile(Model model,HttpSession session){
        UserMaster userMaster = (UserMaster) session.getAttribute("user_master");
        LocalDate today=LocalDate.now();
        int postsCount=postRepository.getCountPostInAWeek(today.plusDays(1).atStartOfDay(),today.minusDays(6).atStartOfDay(),userMaster);
        System.out.println("Start Date : "+today.plusDays(1).atStartOfDay());
        System.out.println("End Date : "+today.minusDays(6).atStartOfDay());

        System.out.println("Total Posts : " + postsCount);
        model.addAttribute("user",userMaster);
        return "profile";
    }

}
