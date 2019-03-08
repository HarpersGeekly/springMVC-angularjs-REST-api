package com.codstrainingapp.trainingapp.models;

import java.time.LocalDateTime;

public class CommentDTO {

    private Long id;
    private String body;
    private LocalDateTime date;

    public CommentDTO(){}

    //======================== relationships =====================

    private Post post;
    private User user;

    //======================== getters & setters =================
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
