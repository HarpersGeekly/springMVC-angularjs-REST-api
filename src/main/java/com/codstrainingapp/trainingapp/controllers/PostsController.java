package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.services.PostService;
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
        return postSvc.findAllOrderByIdDesc();
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

    @GetMapping("/posts/{id}/{title}")
    public String showPostPage(@PathVariable(name="id") long id, @PathVariable(name="title") String title, Model viewModel) {
        Post post = postSvc.findOne(id);
        viewModel.addAttribute("post", post);
        return "posts/show";
    }

    @PostMapping("deletePost/{id}")
    public void deletePost(@PathVariable long id) {
        postSvc.delete(postSvc.findOne(id));
    }

}
