package com.example.ProjectHON.Badge_masterpackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BadgeController {
    @Autowired
    BadgeMasterRepository badgeMasterRepository;
}
