package com.codstrainingapp.trainingapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name="post_votes")
public class PostVote {

    @EmbeddedId
    // "this Class, PostVote, needs to exist and needs an id"
    // but it won't show up in the db, there's no incrementing, and it doesn't change the db at all. So I make PostVoteId
    private PostVoteId id;

    private int type;

    @ManyToOne @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @JsonManagedReference
    private Post post;

    @ManyToOne @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonManagedReference
    private User user;

    public PostVote() {}

    public PostVote(Post post, User user, int type) {
        this.id = new PostVoteId(post.getId(), user.getId());
        this.post = post;
        this.user = user;
        this.type = type;
    }

    public static PostVote vote(Post post, User user, int type) {
        return new PostVote(post, user, type);
    }

    public boolean isUpvote() {
        return type == 1;
    }

    public boolean isDownVote() {
        return type == -1;
    }

    public boolean voteBelongsTo(User user) {
        return this.user.getId().equals(user.getId());
    }

    @JsonIgnore
    public PostVoteId getId() {
        return id;
    }

    public void setId(PostVoteId id) {
        this.id = id;
    }

    @JsonIgnore
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
