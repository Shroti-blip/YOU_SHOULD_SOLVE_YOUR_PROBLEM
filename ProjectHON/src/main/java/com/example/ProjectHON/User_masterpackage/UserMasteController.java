package com.example.ProjectHON.User_masterpackage;

import com.example.ProjectHON.Post_masterpackage.PostMaster;
import com.example.ProjectHON.Post_masterpackage.PostRepository;
import com.example.ProjectHON.Rating_masterpackage.RatingRepository;
import com.example.ProjectHON.SecurityPackage.ValidationConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalTime;
import java.util.*;


@Controller
public class UserMasterController {
    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ValidationConfig validator;

    @Autowired
    ReferralRepository referralRepository;

    @GetMapping("/")
    public String getLogin(){
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String getPage(Model model) {
        model.addAttribute("user", new UserMaster());
        return "usermaster/signup";
    }


    //      @RequestParam("photo") MultipartFile profile_pic,
    @PostMapping("/getotp")
    public String saveInfo(@Valid @ModelAttribute("user") UserMaster user,
                           @RequestParam("referralCode")String referralCode,
                           BindingResult bindingResult,
                           Model model,
                           HttpSession session) {

        System.out.println("=============Inside getOtp mapping .==============");

        if (bindingResult.hasErrors()) {
            return "usermaster/signup";
        }

            try {

                if (userMasterRepository.findByEmail(user.getEmail()).isPresent()) {
                    model.addAttribute("error", "Email address already exists");
                    return "usermaster/signup";
                }


                String otp = emailService.sendOtp(user.getEmail());
                LocalTime currentTime = LocalTime.now();
                System.out.println("Current time is " + currentTime);
                //Add 10 minute more
                LocalTime tenMinutesLater = currentTime.plusMinutes(10);


                System.out.println("Referral Code is "+referralCode);


                session.setAttribute("referralCode2",referralCode);
                session.setAttribute("user", user);
                session.setAttribute("systemOTP", otp);
                session.setAttribute("tenMinutesLater", tenMinutesLater);

//                System.out.println("=============Inside try and catch gor getotp==============");

            } catch (Exception e) {
                System.out.println("here is problem." +e.getMessage());
                e.printStackTrace();
            }


//        System.out.println("=============Before return statement.==============");

        return "usermaster/getotp";
    }

    @PostMapping("/savedata")
    public String saveData(Model model, HttpSession session,
                           @RequestParam("userOtp") int userOtp,
                           RedirectAttributes redirectAttributes) {
        try {
            System.out.println("====================Save Data process=====================");
            UserMaster user = (UserMaster) session.getAttribute("user");
            String systemOTP = (String) session.getAttribute("systemOTP");
            LocalTime tenMinutesLater = (LocalTime) session.getAttribute("tenMinutesLater");

//            String referralCode=(String) session.getAttribute("referralCode2");
            String referralCode=(String) session.getAttribute("referralCode2");
            LocalTime currentTime = LocalTime.now();

            //check if otp is not equal to system one or not.
            if (!systemOTP.equals(String.valueOf(userOtp))) {
                model.addAttribute("user", user);
                model.addAttribute("error", "Invalid Otp");
                System.out.println("=============Invalid otp thing==================");
                return "usermaster/getotp";
            }

            //check if time expires
            if (currentTime.isAfter((tenMinutesLater))) {
                model.addAttribute("user", user);
                model.addAttribute("error", "OTP expired. Request a new one.");
                System.out.println("Inside 10 min. one.");
                return "usermaster/getotp";
            }
//            password problem while registration.
            user.setPassword(passwordEncoder.encode(user.getRawPassword()));
            user.getRoleList().add("ROLE_USER");
//           For generating User Referral code.
            user.setReferralCode(user.getUsername().substring(0,3).toUpperCase()+ UUID.randomUUID().toString().substring(0,5));


            System.out.println("Referral Code inside the savedata method! "+referralCode );
            Optional<UserMaster> referral = userMasterRepository.findByReferralCode(referralCode);

            if(referral.isPresent()){
                //get user who referred their code to the other one.
                UserMaster refer =  referral.get();
                    System.out.println("========== Total points for that code owner ========= " + refer.getPoints());

                //working
                user.setReferredBy(refer);
                user.setReferredByPopup(true);
                // reward the referrer
                refer.setPoints(refer.getPoints()+150);
                userMasterRepository.save(refer);
                //setting points for user who is doing signup.
                user.setPoints(user.getPoints()+50);
                ReferralMaster referralMaster=new ReferralMaster();
                referralMaster.setReferredFromUser(refer);
                userMasterRepository.save(user);
                referralMaster.setReferredToUser(user);
                referralMaster.setShowPopup(false);
                referralRepository.save(referralMaster);
            }
            userMasterRepository.save(user);
            System.out.println("Successful Registration.");
            emailService.sendAfterRegistration(user.getEmail());


        } catch (Exception e) {
            model.addAttribute("error", "Error processing files: " + e.getMessage());
            System.out.println("Exception is here in save data " + e);
        }
        return "usermaster/login";

    }



    @GetMapping("/login")
    public String getLoginPage() {
        return "usermaster/login";
    }

    @GetMapping("/logout")
    public String getLogout() {
        return "usermaster/login";
    }


    //Login code
    @GetMapping("/user/profile")
    public String getLogin(Model model, HttpSession session) {
        System.out.println("Inside login mapping");
        Long userId = (Long) session.getAttribute("userId");

        Optional<UserMaster> userMaster = userMasterRepository.findById(userId);

        if(userMaster.isPresent()) {
            UserMaster user = userMaster.get();
//            System.out.println("============Inside user login mapping============");
            session.setAttribute("user_id", user.getUserId());
            session.setAttribute("user" , user);
            model.addAttribute("user", user);
            model.addAttribute("msg" , "User is Registered.");

            if(user.getReferredBy() != null && user.getReferredByPopup()){
                user.setReferredByPopup(false);
                userMasterRepository.save(user);
//                System.out.println("==========checking condition for popup=============");
                model.addAttribute("popupMessage", "🎉 You earned 50 points for joining with a referral!");
            }

            return "usermaster/user_profile";
        }
        model.addAttribute("error", "Invalid Credentials!");
        return "usermaster/login";

    }


//    for complete user profile


    @PostMapping("/completeProfile")
    public String completeUserProfile(Model model , HttpSession session,
                                      @Valid @ModelAttribute("user") UserMaster userMaster,
                                      BindingResult bindingResult,
                                      @RequestParam("profilePhoto") MultipartFile file){



        //first get session user then fetch db user
        UserMaster sessionUser  = (UserMaster) session.getAttribute("user");

        System.out.println("value of email is : " + sessionUser .getEmail());
        Optional<UserMaster> data = userMasterRepository.findByEmail(sessionUser.getEmail());
//        Optional<UserMaster> data = (Optional)userMasterRepository.findByUsername(sessionUser.getUsername());

        if(bindingResult.hasErrors()){
            if(data.isPresent()){
                userMaster.setProfilePhoto(data.get().getProfilePhoto());
            }
            model.addAttribute("user" , userMaster);
            return  "usermaster/user_profile";
        }
        try{
            if(data .isPresent()){
                UserMaster user = data.get();
                user.setUsername(userMaster.getUsername());
                user.setEmail(userMaster.getEmail());
                user.setContactNo(userMaster.getContactNo());
                user.setBio(userMaster.getBio());
                user.setDateOfBirth(userMaster.getDateOfBirth());
                user.setRelationshipStatus(userMaster.getRelationshipStatus());
                user.setJeoLocation(userMaster.getJeoLocation());
                user.setGender(userMaster.getGender());
                user.setFullName(userMaster.getFullName());
                System.out.println("==========Username is========= : " + userMaster.getFullName());
                if(file != null && !file.isEmpty()){
                    user.setProfilePhoto(file.getBytes());
//                 System.out.println("====getting photos in byte=====");
                }
                //don't have to add joining date and password

                userMasterRepository.save(user);
               // System.out.println("==========================profile update done.==============================");
            }
        } catch (Exception e) {
            System.out.println("===========Exception===========" + e.getMessage());
        }

        return "redirect:/user/rating";
    }


    @GetMapping("/user/googleProfile")
    public String getGoogleProfileInfoPage(HttpSession session , Model model){
        Long sessionUserId =   (Long) session.getAttribute("userId");
        Optional<UserMaster> userMaster = userMasterRepository.findById(sessionUserId);
        model.addAttribute("user" , userMaster);
        return "usermaster/googleProfile";
    }

    @PostMapping("/completeGoogleProfile")
    public String saveGoogleSignUpInfo(HttpSession session , Model model,
                                       @RequestParam("gender") String gender,
                                       @RequestParam("password") String password,
                                       @RequestParam("referralCode") String referralCode,
                                       RedirectAttributes redirectAttributes){

        //first get session user then get db user;
        Long sessionUserId =   (Long) session.getAttribute("userId");
        Optional<UserMaster> userMasterOptional=  userMasterRepository.findById(sessionUserId);

        if (sessionUserId == null) {
            return "redirect:/login";
        }

        try {
            if(userMasterOptional != null){
                UserMaster userMaster1 =userMasterOptional.get();
                userMaster1.setGender(gender);
                userMaster1.setPassword(passwordEncoder.encode(password));
//                  userMaster1.setDateOfBirth(userMaster.getDateOfBirth());
//                  if(file != null && !file.isEmpty()){
//                      userMaster1.setProfilePhoto(file.getBytes());
//                  }
                System.out.println("==========inside google profile for saving code ========");
//                  userMaster1.setReferralCode(referralCode);

                //for referral code points .
                Optional<UserMaster> referral = userMasterRepository.findByReferralCode(referralCode);

                if(referral.isPresent()){
                    //get user who referred their code to the other one.
                    UserMaster refer =  referral.get();
                    System.out.println("========== Total points for that code owner =========" + refer.getPoints());

                    //working
                    userMaster1.setReferredBy(refer);
                    // reward the referrer
                    refer.setPoints(refer.getPoints()+150);

                    userMasterRepository.save(refer);
                    ReferralMaster referralMaster=new ReferralMaster();
                    referralMaster.setReferredFromUser(refer);
                    referralMaster.setReferredToUser(userMaster1);
                    referralMaster.setShowPopup(false);
                    referralRepository.save(referralMaster);
                    //setting points for user who is doing signup.
                    userMaster1.setPoints(userMaster1.getPoints()+50);
                    //working
                    if(userMaster1.getReferredBy() != null){
                        redirectAttributes.addFlashAttribute("popupPoints", "You just earned 50 points for registering with a referral code!");// "🎉 You signed up successfully using a referral code! You and your referrer earned 100 points!");
                    }

                }

                userMasterRepository.save(userMaster1);
                return "redirect:/user/rating";
            }
        }catch (Exception e){
            System.out.println("Exception is : " + e);
        }


        return "usermaster/login";
    }


    @GetMapping("/skipProfile")
    public String skipProfile(Principal principal) {
        String username = principal.getName();
        UserMaster user = userMasterRepository.findByUsername(username).orElseThrow();
        user.setCompleteProfile(false);
        userMasterRepository.save(user);
        return "redirect:/user/rating";
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

    @GetMapping("/check-otp/{otp}")
    @ResponseBody
    public ResponseEntity<Map<String , Boolean>> checkOtp( @PathVariable("otp") String otp,
                                                           Model model , HttpSession session){

        System.out.println("Inside the otp method");
        String sessionOtp = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        LocalTime tenMinLater = (LocalTime) session.getAttribute("tenMinLater");

        Map<String, Boolean> response = new HashMap<>();

        //  Check OTP expiry
        if (LocalTime.now().isAfter(tenMinLater)) {
            model.addAttribute("error", "OTP has expired. Please request a new one.");
            session.invalidate();
            response.put("expiredOtp", true);
        }

        //  Compare OTPs
        if (!sessionOtp.trim().equals(otp.trim())) {
            model.addAttribute("error", "Invalid OTP.");
            response.put("invalidOtp" , true);
        }


        return ResponseEntity.ok(response);
    }


    @PostMapping("/resetPassword")
    @Transactional
    public String resetPassword(Model model,
                                HttpSession session,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword) {

        System.out.println("Inside reset password.");


        String email = (String) session.getAttribute("email");



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



    //    for stopping binding profile manually , image is getting saved in db.
//    or you can just change the name of profilePhoto in html and in requestparam
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("profilePhoto");
    }

//    for showing profile image

    @GetMapping("/userphoto")
    public ResponseEntity<byte[]> userPhoto(Model model , HttpSession session){
        Long userId = (Long) session.getAttribute("user_id");
        UserMaster userMaster = userMasterRepository.findById(userId).orElse(null);

        if(userMaster == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userMaster.getProfilePhoto());
    }


//    for email check onkeyup

    @GetMapping("/check-email")
    @ResponseBody
    public  ResponseEntity<Map<String , Boolean>> checkEmailExists(@RequestParam String email){
        boolean exists = userMasterRepository.findByEmail(email).isPresent();
        Map<String , Boolean> response= new HashMap<>();
        response.put("exists" , exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-username")
    @ResponseBody
    public ResponseEntity<Map<String , Boolean>> checkUserName(@RequestParam("username") String username){
        boolean exists =   userMasterRepository.findByUsername(username).isPresent();
        Map<String , Boolean> response = new HashMap<>();
        response.put("exists" , exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-referralCode")
    @ResponseBody
    public ResponseEntity<Map<String , Boolean>> checkReferralCode(@RequestParam("referralCode") String referralCode){
        boolean exists = userMasterRepository.findByReferralCode(referralCode).isPresent();
        Map<String , Boolean> response = new HashMap<>();
        response.put("exists" , exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/userphoto/{postId}")
    ResponseEntity<byte[]> getUserProfile(@PathVariable("postId")Long postId){

        PostMaster postMaster=postRepository.findById(postId).orElse(null);// Admin Object for session creation
        if(postMaster==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(postMaster.getPhoto());

    }

}




















