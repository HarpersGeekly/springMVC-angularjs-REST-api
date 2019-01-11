package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostVote;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.services.PostService;
import com.codstrainingapp.trainingapp.services.PostVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostsController {

    private PostService postSvc;
    private PostVoteService postVoteSvc;

    @Autowired
    public PostsController(PostService postSvc, PostVoteService postVoteSvc) {
        this.postSvc = postSvc;
        this.postVoteSvc = postVoteSvc;
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

    @PostMapping("/posts/create")
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

    @PostMapping("/deletePost/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable long id) {
        Post post = postSvc.findOne(id);
        postSvc.delete(post);
    }


    @PostMapping("/deletePost/{id}/redirect")
    public String deletePostRedirect(@PathVariable long id, RedirectAttributes redirect) {
        Post post = postSvc.findOne(id);
        postSvc.delete(post);
        return "redirect:/profile";
    }

    @GetMapping("/posts/{id}/{title}")
    public String showPostPage(@PathVariable(name="id") long id, Model viewModel) {
        Post post = postSvc.findOne(id);
        viewModel.addAttribute("post", post);
        return "posts/show";
    }

    @GetMapping("/posts/{id}/edit")
    public String showEditPostPage(@PathVariable(name="id") long id, Model viewModel) {
        Post post = postSvc.findOne(id);
        viewModel.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/posts/edit")
    public String editPost(@Valid @ModelAttribute("post") Post post,
                           BindingResult validation, Model viewModel) {

        Post existingPost = postSvc.findOne(post.getId());

        if(validation.hasErrors()) {
            viewModel.addAttribute("hasErrors", validation.hasErrors());
            return "posts/edit";
        }

        post.setDate(existingPost.getDate());
        post.setUser(existingPost.getUser());
        postSvc.update(post);
        return "redirect:/profile";
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

        System.out.println("post: " + post);
        System.out.println("user: " + user);
        System.out.println("vote type: " + vote);

        if (vote.equalsIgnoreCase("upvote")) {
            post.addVote(PostVote.up(post, user));
            postVoteSvc.save(PostVote.up(post, user));
            System.out.println("up");
        } else {
            post.addVote(PostVote.down(post, user));
            postVoteSvc.save(PostVote.down(post, user));
            System.out.println("down");
        }

        System.out.println("get here: " + post.getPostVotes());
        return post;
    }

}
