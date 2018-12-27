package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostsController {

    private PostService postSvc;

    @Autowired
    public PostsController(PostService postSvc) {
        this.postSvc = postSvc;
    }

    @GetMapping("/")
    public String index(Model viewModel) {
        System.out.println("get to Home Page");
        viewModel.addAttribute("posts", postSvc.findAll());
        return "index";
    }
}
