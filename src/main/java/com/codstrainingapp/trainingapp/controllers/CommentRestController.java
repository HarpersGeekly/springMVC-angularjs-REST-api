package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.CommentDTO;
import com.codstrainingapp.trainingapp.services.CommentService;
import com.codstrainingapp.trainingapp.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentRestController {

    private CommentService commentSvc;

    @Autowired
    public CommentRestController(CommentService commentSvc) {
        this.commentSvc = commentSvc;
    }

    // ------------------- Find Comment(s) ------------------------------

    @GetMapping("/commentsByPost/{postId}")
    public List<CommentDTO> getCommentsByPost(@PathVariable Long postId) {
        try {
            commentSvc.findAllByPostId(postId);
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
        return commentSvc.findAllByPostId(postId);
    }

    @GetMapping("/commentsByUser/{userId}")
    public List<CommentDTO> getCommentsByUser(@PathVariable Long userId) {
        try {
            commentSvc.findAllByUserId(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
        return commentSvc.findAllByUserId(userId);
    }

    // ------------------- Save Comment --------------------------------

    @PostMapping("/saveComment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO saveComment(@RequestBody CommentDTO comment) {
        System.out.println("comment coming over " + comment);
        comment.setDate(LocalDateTime.now());
        return commentSvc.saveComment(comment);
    }

    // ------------------- Delete Comment ------------------------------

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            CommentDTO commentDto = commentSvc.findOne(id);
            commentSvc.delete(commentDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ------------------- Edit Comment ------------------------------



}
