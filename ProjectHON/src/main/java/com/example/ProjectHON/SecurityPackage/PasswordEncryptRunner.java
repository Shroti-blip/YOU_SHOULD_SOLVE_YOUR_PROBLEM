package com.example.ProjectHON.SecurityPackage;

import com.example.ProjectHON.User_masterpackage.UserMaster;
import com.example.ProjectHON.User_masterpackage.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PasswordEncryptRunner implements CommandLineRunner {

    @Autowired
    private UserMasterRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<UserMaster> users = userRepository.findAll();
        for (UserMaster user : users) {
            // Check if password already encrypted
            if (!user.getPassword().startsWith("$2a$")) { // BCrypt encrypted password starts with $2a$
                String encoded = passwordEncoder.encode(user.getPassword());
                user.setPassword(encoded);
                userRepository.save(user);
            }
        }
        System.out.println("All existing passwords are now encrypted!");
    }
}