package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostVote;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.services.PostService;
import com.codstrainingapp.trainingapp.services.PostVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController // @RestController = @Controller + @ResponseBody (returns jackson json string) instead of annotating methods with @ResponseBody
@RequestMapping("/api/post")
@CrossOrigin(origins = "http://localhost:8080")// https://spring.io/blog/2015/06/08/cors-support-in-spring-framework  Access to XMLHttpRequest at 'http://localhost:8888/posts' from origin 'http://localhost:8080' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
public class PostsRestController {

    private PostService postSvc;
    private PostVoteService postVoteSvc;

    @Autowired
    public PostsRestController(PostService postSvc, PostVoteService postVoteSvc) {
        this.postSvc = postSvc;
        this.postVoteSvc = postVoteSvc;
    }

    @GetMapping("/posts")
    public List<Post> fetchPosts() {
        return postSvc.findAll();
    }

    @GetMapping(value = "/id/{id}")
    public Post findById(@PathVariable(name = "id") Long id) {
        return postSvc.findOne(id);
    }

    @PostMapping(value = "/save")
    public void savePost(Post post) {
        System.out.println("get here save");
        postSvc.save(post);
    }

    @DeleteMapping("/deletePost/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable long id) {
        Post post = postSvc.findOne(id);
        postSvc.delete(post);
    }

    @PostMapping("/deletePost/{id}/redirect")
    public String deletePostRedirect(@PathVariable long id) {
        Post post = postSvc.findOne(id);
        postSvc.delete(post);
        return "redirect:http://localhost:8080/profile";
    }

//    @PostMapping("/posts/edit")
//    public String editPost(@Valid @ModelAttribute("post") Post post,
//                           BindingResult validation, Model viewModel) {
//
//        Post existingPost = postSvc.findOne(post.getId());
//
//        if(validation.hasErrors()) {
//            viewModel.addAttribute("hasErrors", validation.hasErrors());
//            return "posts/edit";
//        }
//
//        post.setDate(existingPost.getDate());
//        post.setUser(existingPost.getUser());
//        postSvc.update(post);
//        return "redirect:http://localhost:8080/profile";
//    }

//    =============== post votes ================

    @PostMapping("/posts/{id}/{type}")
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

//    @GetMapping("/")
//    public String index() {
//        System.out.println("get to Home Page");
//        return "index";
//    }
//    @GetMapping("/posts/create")
//    public String showCreatePostForm(Model viewModel) {
//        viewModel.addAttribute("post", new Post());
//        return "/posts/create";
//    }
//    @GetMapping("/posts/{id}/{title}")
//    public String showPostPage(@PathVariable(name="id") long id, Model viewModel) {
//        Post post = postSvc.findOne(id);
//        viewModel.addAttribute("post", post);
//        return "posts/show";
//    }
//
//    @GetMapping("/posts/{id}/edit")
//    public String showEditPostPage(@PathVariable(name="id") long id, Model viewModel) {
//        Post post = postSvc.findOne(id);
//        viewModel.addAttribute("post", post);
//        return "posts/edit";
//    }
}
