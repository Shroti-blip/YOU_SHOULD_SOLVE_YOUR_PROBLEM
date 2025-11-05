package com.example.ProjectHON.SecurityPackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
//       logger.info(user.getName());
//
//       user.getAttributes().forEach((key , value) ->{
//           logger.info("{} => {}",key, value);
//       });
//
//       logger.info(user.getAttributes().toString());

        //saving data on db.

       String email =  user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("picture").toString();
        String firstName = name.split(" ")[0];



        String baseUsername = firstName.toLowerCase() + "_g"; // => "muskan_g"
        String username = baseUsername;

// 
        int counter = 1;
        while (userMasterRepository.findByUsername(username).isPresent()) {
            username = baseUsername + counter; // "muskan_g1"
            counter++;
        }

        //creating a new obj of Usermaster.

        UserMaster userMaster = new UserMaster();
//        userMaster.setFullName(name);
        userMaster.setUsername(baseUsername);
        userMaster.setEmail(email);
        userMaster.setProfilePhoto(picture.getBytes());
//        userMaster.setUserId(UUID.randomUUID().node());
        userMaster.setRoleList(List.of("ROLE_USER"));
        userMaster.setLoginProvider("GOOGLE");

        HttpSession session=request.getSession();
       UserMaster userMaster1 = userMasterRepository.findByEmail(email).orElse(null);

       if(userMaster1 == null){
           userMasterRepository.save(userMaster);
           logger.info(user.getAttribute("name"));

           session.setAttribute("userId",userMaster.getUserId());
       }
       else{
           session.setAttribute("userId",userMaster1.getUserId());
       }




        new DefaultRedirectStrategy().sendRedirect(request,response,"/user/profile");
//        new DefaultRedirectStrategy().sendRedirect(request,response,"user/profile");
    }
}
