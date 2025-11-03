package com.example.ProjectHON.SecurityPackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserMaster user = userMasterRepository.findByEmail(email).orElse(null);
        UserMaster user = userMasterRepository.findByUsername(username);
        if(user!=null) {
            return user;
        }
        throw new UsernameNotFoundException("User not found with username "+username);
    }
}
