package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostVote;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.services.PostService;
import com.codstrainingapp.trainingapp.services.PostVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(value = "/postById/{id}")
    public Post findById(@PathVariable(name = "id") Long id) {
        return postSvc.findOne(id);
    }

    @GetMapping(value = "/postsByUserId/{id}")
    public List<Post> findAllByUserId(@PathVariable(name = "id") Long id) {
        return postSvc.findAllByUserId(id);
    }

    @PostMapping(value = "/save")
    public void savePost(Post post) {
        System.out.println("get here save");
        postSvc.savePost(post);
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



}
