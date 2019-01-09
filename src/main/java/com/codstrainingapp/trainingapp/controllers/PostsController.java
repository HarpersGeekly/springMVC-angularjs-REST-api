package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostVote;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.services.PostService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostsController {

    private PostService postSvc;

    @Autowired
    public PostsController(PostService postSvc) {
        this.postSvc = postSvc;
    }

    @GetMapping("/")
    public String index() {
        System.out.println("get to Home Page");
        return "index";
    }

    @GetMapping("/posts")
    @ResponseBody //returns jackson json string
    public List<Post> fetchPosts() {
        return postSvc.findAll();
    }

    @GetMapping("/posts/create")
    public String showCreatePostForm(Model viewModel) {
        viewModel.addAttribute("post", new Post());
        return "/posts/create";
    }

    @PostMapping("posts/create")
    public String createPost(@Valid @ModelAttribute("post") Post post,
                             BindingResult validation, Model viewModel, HttpServletRequest request) {

        if(validation.hasErrors()) {
            viewModel.addAttribute("hasErrors", validation.hasErrors());
            return "/posts/create";
        }

        User user = (User) request.getSession().getAttribute("user");
        post.setUser(user);
        post.setDate(LocalDateTime.now());
        postSvc.save(post);
        return "redirect:/";
    }

    @PostMapping("deletePost/{id}")
    public void deletePost(@PathVariable long id) {
        Post post = postSvc.findOne(id);
        postSvc.delete(post);
    }

    @GetMapping("/posts/{id}/{title}")
    public String showPostPage(@PathVariable(name="id") long id, Model viewModel) {
        Post post = postSvc.findOne(id);
        viewModel.addAttribute("post", post);
        return "posts/show";
    }

    @GetMapping("/posts/fetch/{id}")
    @ResponseBody
    public Post fetchPost(@PathVariable(name="id") long id) {
        return postSvc.findOne(id);
    }

//    =============== post votes ================

    @PostMapping("/posts/{id}/{type}")
    @ResponseBody
    public Post vote(@PathVariable(name="id") long id, @PathVariable(name="type") String vote, HttpServletRequest request) {

        Post post = postSvc.findOne(id);
        User user = (User) request.getSession().getAttribute("user");

        if(vote.equals("upvote")) {
            PostVote.vote(post, user, 1);
        } else {
            PostVote.vote(post, user, -1);
        }

        postSvc.save(post);
        return post;
    }

}
