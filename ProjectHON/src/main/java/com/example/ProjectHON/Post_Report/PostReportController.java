package com.example.ProjectHON.Post_Report;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.User_masterpackage.UserMaster;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class PostReportController {

    @Autowired
    PostReportRepository postReportRepository;

    @Autowired
    PostRepository postRepository;



    @PostMapping("/user/reportPost")
    public String saveReport(@RequestParam("reason") String reason ,
                             @RequestParam("comment") String comment,
                             @RequestParam("postId") Long postId,
                             HttpSession session){

        System.out.println("====================Inside report Post=============");
        //login user id who is reporting
        UserMaster reporter = (UserMaster) session.getAttribute("user_master");
        Long userId = reporter.getUserId();
        PostMaster postMaster = postRepository.findById(postId).orElse(null);

        Boolean alreadyExists =  postReportRepository.existsByReporterUserIdAndPostreport_PostId(userId , postId );

        if(alreadyExists){
            System.out.println("=====😒😒😒😒😒😒😒😒😒=====already exists====");
            return "redirect:/user/rating?duplicateReport=true";
        }

        //        Long postImageId = postMaster.getPostId();
        PostReport postReport = new PostReport();
        postReport.setReason(reason);
        postReport.setComment(comment);
        postReport.setContentType("IMAGES");
        postReport.setTimestamp(LocalDate.now());
        postReport.setPostreport(postMaster);
        postReport.setReporter(reporter);
        postReport.setOffender(postMaster.getUser());
        postReportRepository.save(postReport);
        System.out.println("====================After report Post=============");

        return "redirect:/user/rating?reportSuccess=true";
    }

}

