package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostDTO;
import com.codstrainingapp.trainingapp.models.PostVote;
import com.codstrainingapp.trainingapp.models.UserDTO;
import com.codstrainingapp.trainingapp.services.PostService;
import com.codstrainingapp.trainingapp.services.PostVoteService;
import com.codstrainingapp.trainingapp.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
        try {
            postSvc.findOne(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
        return postSvc.findOne(id);
    }

    @GetMapping("/postsByUserId/{id}")
    public List<PostDTO> findAllByUserId(@PathVariable(name = "id") Long id) {
        try {
            postSvc.findAllByUserId(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
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

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            PostDTO user = postSvc.findOne(id);
            postSvc.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/posts/{type}/{id}")
    @ResponseBody
    public PostDTO postVoting(@PathVariable Long id, @PathVariable String type,
                    Authentication token) {

        PostDTO post = postSvc.findOne(id);
        UserDTO user = (UserDTO) token.getPrincipal(); //userSvc.loggedInUser()));

        if (type.equalsIgnoreCase("upvote")) {
//            post.addVote(PostVote.up(post, user));
        } else {
//            post.addVote(PostVote.down(post, user));
        }

        postSvc.savePost(post);
        return post;
    }

//    @PostMapping("/posts/{postId}/removeVote")
//    public @ResponseBody
//    Post voteRemoval(@PathVariable Long postId) {
//
//        PostDTO post = postSvc.findOne(postId);
//        UserDTO user = userSvc.loggedInUser();

//        List<PostVote> votes = post.getVotes();
//        System.out.println("vote count:" + post.voteCount());

//        for (PostVote vote : votes) {
////            if (vote.getUser().getId() == (user.getId())) {
//            if (vote.voteBelongsTo(user)) {
//                post.removeVote(vote);
//                postVoteSvc.delete(vote);
//                postSvc.save(post);
//                System.out.println("vote count:" + post.voteCount());
//                break;
//            }
//        }
//        postSvc.save(post);
//        return post;
//    }
}
