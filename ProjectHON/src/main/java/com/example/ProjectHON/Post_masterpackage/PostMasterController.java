package com.example.ProjectHON.Post_masterpackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PostMasterController {
    @Autowired
    PostRepository postRepository;
}
