package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.*;
import com.codstrainingapp.trainingapp.services.PostService;
import com.codstrainingapp.trainingapp.services.PostVoteService;
import com.codstrainingapp.trainingapp.services.UserService;
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
    private UserService userSvc;

    @Autowired
    public PostsRestController(PostService postSvc, PostVoteService postVoteSvc, UserService userSvc) {
        this.postSvc = postSvc;
        this.postVoteSvc = postVoteSvc;
        this.userSvc = userSvc;
    }

    @GetMapping("/posts")
    public List<PostDTO> findAll() {
        return postSvc.findAll();
    }

    @GetMapping("/postById/{id}")
    public PostDTO findById(@PathVariable(name = "id") Long id, @RequestParam(name = "userId", required=false) Long userId) {
        try {
            PostDTO postDto = postSvc.findOne(id);
            if (userId != null) {
                UserDTO userDto = userSvc.findOne(userId);
                postDto.getVoteFrom(userSvc.convertToUser(userDto));
                return postDto;
            }
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

    @PostMapping("/postVote/{postId}/{userId}/{type}")
    public PostDTO postVoting(@PathVariable Long postId, @PathVariable Long userId, @PathVariable int type) {
        PostDTO postDto = postSvc.findOne(postId);
        UserDTO userDto = userSvc.findOne(userId);
        Post post = postSvc.convertToPost(postDto);
        User user = userSvc.convertToUser(userDto);
        if (type == 1) {
            post.addVote(PostVote.up(post, user));
        } else {
            post.addVote(PostVote.down(post, user));
        }
        postSvc.savePost(postDto);
        return postDto;
    }

    @PostMapping("/removeVote/{postId}/{userId}")
    public PostDTO voteRemoval(@PathVariable Long postId, @PathVariable Long userId) {
        UserDTO userDto = userSvc.findOne(userId);
        PostDTO postDto = postSvc.findOne(postId);
        User user = userSvc.convertToUser(userDto);
        Post post = postSvc.convertToPost(postDto);
        List<PostVote> votes = post.getPostVotes();

        for (PostVote vote : votes) {
            if (vote.voteBelongsTo(user)) {
                post.removeVote(vote);
                postVoteSvc.deletePostVote(vote);
                postSvc.savePost(postDto);
                break;
            }
        }
        postSvc.savePost(postDto);
        return postDto;
    }
}
