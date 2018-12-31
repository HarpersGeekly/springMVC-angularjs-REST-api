package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("deletePost/{id}")
    public void deletePost(@PathVariable long id) {
        postSvc.delete(postSvc.findOne(id));
    }

}
