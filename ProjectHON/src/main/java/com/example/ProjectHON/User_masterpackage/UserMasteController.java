package com.example.ProjectHON.User_masterpackage;

import com.example.ProjectHON.SecurityPackage.ValidationConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserMasteController {
    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    private ValidationConfig validator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String getPage(Model model){
        model.addAttribute("user" , new UserMaster());
        return "usermaster/signup";
    }

//      @RequestParam("photo") MultipartFile profile_pic,
    @PostMapping("/getotp")
    public String saveInfo(@Valid @ModelAttribute("user") UserMaster user,
                           BindingResult bindingResult,
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
//                if(profile_pic != null && !profile_pic.isEmpty()){
//                    user.setProfilePhoto(profile_pic.getBytes());
//                }


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
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMasterRepository.save(user);
            System.out.println("Successful Registration.");
            emailService.sendAfterRegistration(user.getEmail());
        }catch (Exception e){
            model.addAttribute("error", "Error processing files: " + e.getMessage());
            System.out.println("Exception is here in save data " + e);
        }
        return "usermaster/login";

    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "usermaster/login";
    }


    @PostMapping("/getloggedin")
    public String getLogin(Model model, HttpSession session,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password) {

        System.out.println("Inside login mapping");

        Optional<UserMaster> optional = userMasterRepository.findByEmail(email);

        if (optional.isPresent()) {
            UserMaster user = optional.get();

            //  Match raw password with encoded password in DB
            if (passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("user", user);
                model.addAttribute("user", user);
                System.out.println("User is present and password matched.");
                return "usermaster/user_profile";
            } else {
                model.addAttribute("error", "Invalid email or password.");
                System.out.println("Password mismatch.");
                return "usermaster/login";
            }
        }

        System.out.println("User does not exist.");
        model.addAttribute("error", "Invalid email or password.");
        return "usermaster/login";
    }



    @GetMapping("/forgetpassword")
    public String forgetPassword(){
        return "usermaster/forgetPassword";
    }

    @PostMapping("/getEmail")
    public String getEmail(Model model , HttpSession session ,
                           String email ){

        try{
            if(userMasterRepository.findByEmail(email).isPresent()){
                //generate otp and send that to user.
                String otp =emailService.sendResetPasswordOtp(email);
                System.out.println("otp for update password : " + otp);
                LocalTime currentTime = LocalTime.now();
                LocalTime tenMinLater = currentTime.plusMinutes(10);

                session.setAttribute("otp" , otp);
                session.setAttribute("email" , email);
                session.setAttribute("systemTime" , currentTime);
                session.setAttribute("tenMinLater" , tenMinLater);
                return "usermaster/updatePassword";
            }
        }catch (Exception e){
            System.out.println("Exception here." + e.getMessage());
        }
        System.out.println("Print here");
        model.addAttribute("error" , "Email does not exist.");
        return "usermaster/forgetPassword";

    }


//    @PostMapping("/resetPassword")
//    @Transactional
//    public String resetPassword(Model model, @RequestParam("otp") String otp, HttpSession session,
//                                @RequestParam("newPassword") String newPassword,
//                                @RequestParam("confirmPassword") String confirmPassword) {
//        System.out.println("Inside reset password.");
//
//
//        String sessionOtp = (String) session.getAttribute("otp");
//        String email = (String)session.getAttribute("email");
//        LocalTime timeSent = (LocalTime) session.getAttribute("systemTime");
//        LocalTime tenMinLater = (LocalTime) session.getAttribute("tenMinLater");
//
//        // basic null checks
//        if (sessionOtp == null || tenMinLater == null || email == null) {
//            model.addAttribute("error", "Session expired. Please request a new OTP.");
//            return "usermaster/forgetPassword";
//        }
//
//        // trim user input and session otp to avoid whitespace mismatch
//        String userOtp = otp == null ? "" : otp.trim();
//        String expectedOtp = sessionOtp.trim();
//
//        // check expiry
//        if (LocalTime.now().isAfter(tenMinLater)) {
//            model.addAttribute("error", "OTP has expired. Please request a new one.");
//            session.invalidate();
//            return "usermaster/forgetPassword";
//        }
//
//        // compare OTPs
//        if (!sessionOtp.equals(otp.trim())) {
//            model.addAttribute("error", "Invalid OTP.");
//            return "usermaster/updatePassword";
//        }
//
//        if (!newPassword.equals(confirmPassword)) {
//            model.addAttribute("error", "Passwords do not match.");
//            return "usermaster/updatePassword";
//        }
//
////        if (newPassword.length() < 6) {
////            model.addAttribute("error", "Password must be at least 6 characters long.");
////            return "usermaster/updatePassword";
////        }
//
//        try{
//            Optional<UserMaster> userOpt = userMasterRepository.findByEmail(email);
//            System.out.println("Inside try and catch.");
//            if(userOpt.isPresent()){
//
//                if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#^_])[A-Za-z\\d@$!%*?&#^_]{6,15}$")) {
//                    model.addAttribute("error", "Password must be 6–15 characters long and include at least 1 letter, 1 number, and 1 special character.");
//                    return "usermaster/updatePassword";
//                }
//
//
//
//                //encrypt before saving.
//                String encryptedPassword = passwordEncoder.encode(newPassword);
//                System.out.println("=================Inside saving process=============.");
//                UserMaster userMaster = userOpt.get();
//                userMaster.setPassword(encryptedPassword);
//                userMasterRepository.save(userMaster);
//                System.out.println("================Inside try and catch save data===============");
//                model.addAttribute("success", "Password reset successfully. Please login.");
//                return "usermaster/login";
//            }
//            else {
//                model.addAttribute("error", "User not found.");
//                return "usermaster/forgetPassword";
//            }
//
//        }catch (Exception e){
//            model.addAttribute("error", "Error updating password: " + e.getMessage());
//            return "usermaster/updatePassword";
//        }
//
//    }
@PostMapping("/resetPassword")
@Transactional
public String resetPassword(Model model,
                            @RequestParam("otp") String otp,
                            HttpSession session,
                            @RequestParam("newPassword") String newPassword,
                            @RequestParam("confirmPassword") String confirmPassword) {

    System.out.println("Inside reset password.");

    String sessionOtp = (String) session.getAttribute("otp");
    String email = (String) session.getAttribute("email");
    LocalTime tenMinLater = (LocalTime) session.getAttribute("tenMinLater");

    //  Basic null checks
    if (sessionOtp == null || tenMinLater == null || email == null) {
        model.addAttribute("error", "Session expired. Please request a new OTP.");
        return "usermaster/forgetPassword";
    }

    //  Check OTP expiry
    if (LocalTime.now().isAfter(tenMinLater)) {
        model.addAttribute("error", "OTP has expired. Please request a new one.");
        session.invalidate();
        return "usermaster/forgetPassword";
    }

    //  Compare OTPs
    if (!sessionOtp.trim().equals(otp.trim())) {
        model.addAttribute("error", "Invalid OTP.");
        return "usermaster/updatePassword";
    }

    //  Match both passwords
    if (!newPassword.equals(confirmPassword)) {
        model.addAttribute("error", "Passwords do not match.");
        return "usermaster/updatePassword";
    }

    try {
        Optional<UserMaster> userOpt = userMasterRepository.findByEmail(email);
        System.out.println("Inside try and catch.");

        if (userOpt.isPresent()) {
            UserMaster userMaster = userOpt.get();

            //  Set raw password for validation
            userMaster.setRawPassword(newPassword);

            // Validate the raw password using Bean Validation
            Set<ConstraintViolation<UserMaster>> violations = validator.validator().validateProperty(userMaster, "rawPassword");
            if (!violations.isEmpty()) {
                String errorMessage = violations.iterator().next().getMessage();
                model.addAttribute("error", errorMessage);
                return "usermaster/updatePassword";
            }

            //  Encrypt before saving
            String encryptedPassword = passwordEncoder.encode(newPassword);
            userMaster.setPassword(encryptedPassword);

            System.out.println("=================Inside saving process=============.");
            userMasterRepository.save(userMaster);
            System.out.println("================Inside try and catch save data===============");

            model.addAttribute("success", "Password reset successfully. Please login.");
            return "usermaster/login";
        } else {
            model.addAttribute("error", "User not found.");
            return "usermaster/forgetPassword";
        }

    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Error updating password: " + e.getMessage());
        return "usermaster/updatePassword";
    }
}

    @GetMapping("/resendOTP")
    @ResponseBody
    public Map<String, String> resendOTP(HttpSession session) {
        Map<String, String> response = new HashMap<>();
        try {
            UserMaster user = (UserMaster) session.getAttribute("user");
            String otp = emailService.sendOtp(user.getEmail());

            LocalTime currentTime = LocalTime.now();
            LocalTime tenMinutesLater = currentTime.plusMinutes(10);

            session.setAttribute("systemOTP", otp);
            session.setAttribute("tenMinutesLater", tenMinutesLater);

            response.put("status", "success");
            response.put("message", "OTP has been sent again!");

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to resend OTP. Please try again.");
        }

        return response;
    }



//    for complete user profile

    @GetMapping("/getuserprofile")
    public String getUserProfile(Model model  , HttpSession session){
    UserMaster user = (UserMaster)session.getAttribute("user");
//    model.addAttribute("userdata" , new UserMaster());
    model.addAttribute("user" , user);
    return "usermaster/user_profile";
    }

//    @PutMapping("/completeuserprofile")
//    public String completeUserProfile(Model model , HttpSession session){
//
//    return "usermaster/update";
//    }

}
