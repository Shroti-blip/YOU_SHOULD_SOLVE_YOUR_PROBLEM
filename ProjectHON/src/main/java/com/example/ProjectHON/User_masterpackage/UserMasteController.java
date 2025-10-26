package com.example.ProjectHON.User_masterpackage;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

@Controller
public class UserMasteController {
    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    EmailService emailService;

    @GetMapping("/getpage")
    public String getPage(Model model){
        model.addAttribute("user" , new UserMaster());
        return "usermaster/signup";
    }

    @PostMapping("/getotp")
    public String saveInfo(@Valid @ModelAttribute("user") UserMaster user,
                           BindingResult bindingResult,
                           @RequestParam("photo") MultipartFile profile_pic,
                           Model model,
                           HttpSession session){
        if(bindingResult.hasErrors()){
            return "usermaster/signup";
        }
        else{

            //check age of user
            if(user.getDateOfBirth() != null){
                int age = Period.between(user.getDateOfBirth() , LocalDate.now()).getYears();
                if(age < 18){
                    model.addAttribute("errorMessage" , "You must be at least 18 years old to register.");
                    return "usermaster/signup";
                }
            }


               try{

                   if(userMasterRepository.findByEmail(user.getEmail()).isPresent()){
                       model.addAttribute("error", "Email address already exists");
                        return "usermaster/signup";
                   }
                if(profile_pic != null && !profile_pic.isEmpty()){
                    user.setProfilePhoto(profile_pic.getBytes());
                }


                String otp =emailService.sendOtp(user.getEmail());
                LocalTime currentTime =LocalTime.now();
                System.out.println("Current time is " + currentTime);
                //Add 10 minute more
                LocalTime tenMinutesLater = currentTime.plusMinutes(10);

                   session.setAttribute("user" , user);
                   session.setAttribute("systemOTP" , otp);
                   session.setAttribute("tenMinutesLater" , tenMinutesLater);


            }catch (Exception e){
                System.out.println("here is problem.");
            }
        }
        return "usermaster/getotp";
    }

    @PostMapping("/savedata")
    public String saveData(Model model , HttpSession session ,
                           @RequestParam("userOtp") int userOtp){
        try{
            System.out.println("====================Save Data process=====================");
            UserMaster user = (UserMaster)session.getAttribute("user");
            String systemOTP = (String)session.getAttribute("systemOTP");
            LocalTime tenMinutesLater = (LocalTime)session.getAttribute("tenMinutesLater");

            LocalTime currentTime = LocalTime.now();

            //check if otp is not equal to system one or not.
            if(!systemOTP.equals(String.valueOf(userOtp))){
            model.addAttribute("user" , user);
            model.addAttribute("error" , "Invalid Otp");
                System.out.println("=============Invalid otp thing==================");
            return "usermaster/getotp";
            }

            //check if time expires
            if(currentTime.isAfter((tenMinutesLater))){
                model.addAttribute("user" , user);
                model.addAttribute("error" , "OTP expired. Request a new one.");
                System.out.println("Inside 10 min. one.");
                return "usermaster/getotp";
            }

            userMasterRepository.save(user);
            System.out.println("Successful Registration.");
            emailService.sendAfterRegistration(user.getEmail());
        }catch (Exception e){
            model.addAttribute("error", "Error processing files: " + e.getMessage());
            System.out.println("Exception is here in save data " + e);
        }
        return "usermaster/login";

    }




}
