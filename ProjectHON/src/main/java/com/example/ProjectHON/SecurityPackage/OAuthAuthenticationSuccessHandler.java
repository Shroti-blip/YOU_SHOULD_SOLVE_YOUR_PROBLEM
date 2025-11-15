package com.example.ProjectHON.SecurityPackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;


@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserMasterRepository userMasterRepository;


    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication ) throws IOException, ServletException {
        logger.info("OAuthAuthenticationSuccessHandler");

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();




//for printing in console.
       logger.info(user.getName());

       user.getAttributes().forEach((key , value) ->{
           logger.info("{} => {}",key, value);
       });

       logger.info(user.getAttributes().toString());

        //saving data on db.

       String email =  user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String imageUrl = user.getAttribute("picture").toString();
//        String gender =  user.getAttribute("gender").toString();
        String firstName = name.split(" ")[0];

        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);



        String baseUsername = firstName.toLowerCase() + "_g"; // => "muskan_g"
        String username = baseUsername;

// for username to not to get same
        int counter = 1;
        while (userMasterRepository.findByUsername(username).isPresent()) {
            username = baseUsername + counter; // "muskan_g1"
            counter++;
        }

        //creating a new obj of Usermaster.



        HttpSession session=request.getSession();
       UserMaster userMaster1 = userMasterRepository.findByEmail(email).orElse(null);

       UserMaster userMaster;
       if(userMaster1 == null){
           userMaster = new UserMaster();
           userMaster.setUsername(baseUsername);
           userMaster.setEmail(email);
//           userMaster.setGender(gender);
           userMaster.setProfilePhoto(imageBytes);
           String referralCode=username.substring(0,3).toUpperCase()+UUID.randomUUID().toString().substring(0,5);
           System.out.println("Referral Code : "+referralCode);
           userMaster.setReferralCode(referralCode);
           System.out.println("==========referal code is ==========" + userMaster.getReferralCode());
           userMaster.setRoleList(List.of("ROLE_USER"));
           userMaster.setLoginProvider("GOOGLE");
           userMasterRepository.save(userMaster);
           logger.info(user.getAttribute("name"));

       }
       else{
            userMaster = userMaster1;
       }


        session.setAttribute("userId",userMaster.getUserId());

        // Replace OAuth2User with your custom UserMaster
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userMaster, null, userMaster.getAuthorities());

        // Store authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //redirect to mapping directly
        if(userMaster1==null || userMaster1.getPassword()==null) {
            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/googleProfile");
        }else{
            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/home");
        }
//        new DefaultRedirectStrategy().sendRedirect(request,response,"user/profile");
    }

//    public byte[] downloadImage(String imageUrl) throws IOException {
//        try (InputStream in = new URL(imageUrl).openStream()) {
//            return in.readAllBytes();
//        }
//    }
}










