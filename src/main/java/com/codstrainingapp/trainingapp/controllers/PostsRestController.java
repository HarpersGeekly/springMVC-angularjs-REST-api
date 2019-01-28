package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostDTO;
import com.codstrainingapp.trainingapp.services.PostService;
import com.codstrainingapp.trainingapp.services.PostVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public List<Post> findAll() {
        return postSvc.findAll();
    }

    @GetMapping("/postById/{id}")
    public Post findById(@PathVariable(name = "id") Long id) {
        return postSvc.findOne(id);
    }

    @GetMapping(value = "/postsByUserId/{id}")
    public List<Post> findAllByUserId(@PathVariable(name = "id") Long id) {
        return postSvc.findAllByUserId(id);
    }

// --------------- Save Post ---------------------------------

    @PostMapping("/savePost")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO savePost(@RequestBody PostDTO post) {
        System.out.println("get here save Post");
        return postSvc.savePost(post);
    }

// ---------------- Update Post ------------------------------

    @PutMapping("/editPost")
    public Post editPost(@RequestBody Post post) {
        return postSvc.updatePost(post);
    }

// ---------------- Delete Post ------------------------------

    @DeleteMapping("/deletePost")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@RequestBody Post post) {
        postSvc.delete(post);
    }

//    @DeleteMapping("/deletePost/{id}/redirect")
//    public String deletePostRedirect(@PathVariable long id) {
//        Post post = postSvc.findOne(id);
//        postSvc.delete(post);
//        return "redirect:http://localhost:8080/profile";
//    }
}
