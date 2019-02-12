package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostDTO;
import com.codstrainingapp.trainingapp.services.PostService;
import com.codstrainingapp.trainingapp.services.PostVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController // @RestController = @Controller + @ResponseBody (returns jackson json string) instead of annotating methods with @ResponseBody
@RequestMapping("/api/post")
@CrossOrigin// https://spring.io/blog/2015/06/08/cors-support-in-spring-framework  Access to XMLHttpRequest at 'http://localhost:8888/posts' from origin 'http://localhost:8080' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
// @CrossOrigin(origins = "localhost:8080") only needed if I wanted just that caller
public class PostsRestController {

    private PostService postSvc;
    private PostVoteService postVoteSvc;

    @Autowired
    public PostsRestController(PostService postSvc, PostVoteService postVoteSvc) {
        this.postSvc = postSvc;
        this.postVoteSvc = postVoteSvc;
    }

    @GetMapping("/posts")
    public List<PostDTO> findAll() {
        return postSvc.findAll();
    }

    @GetMapping("/postById/{id}")
    public PostDTO findById(@PathVariable(name = "id") Long id) {
        return postSvc.findOne(id);
    }

    @GetMapping("/postsByUserId/{id}")
    public List<PostDTO> findAllByUserId(@PathVariable(name = "id") Long id) {
        return postSvc.findAllByUserId(id);
    }

// --------------- Save Post ---------------------------------

    @PostMapping("/savePost")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO savePost(@RequestBody PostDTO post) {
        post.setDate(LocalDateTime.now());
        return postSvc.savePost(post);
    }

// ---------------- Update Post ------------------------------

    @PutMapping("/editPost")
    public PostDTO editPost(@RequestBody PostDTO post) {
        return postSvc.updatePost(post);
    }

// ---------------- Delete Post ------------------------------

    @DeleteMapping("/deletePost")
    public void deletePost(@RequestBody PostDTO post) {
        postSvc.delete(post);
    }

//    @DeleteMapping("/deletePost/{id}/redirect")
//    public String deletePostRedirect(@PathVariable long id) {
//        Post post = postSvc.findOne(id);
//        postSvc.delete(post);
//        return "redirect:http://localhost:8080/profile";
//    }
}
