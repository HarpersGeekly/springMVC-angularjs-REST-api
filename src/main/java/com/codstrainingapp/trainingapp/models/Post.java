package com.codstrainingapp.trainingapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(name = "posts") //required
public class Post {

    @Id // required. Hibernate maps this attribute to a table column named "id"
    @GeneratedValue(strategy = GenerationType.IDENTITY) //enable auto ID generation
    private Long id;
    private String title;
    private String subtitle;                            // } maps attributes automagically
    private String leadImage;
    private String body;
    @Column(name="created_date")
    private LocalDateTime date;

    public Post() {}

    public Post(Long id, String title, String subtitle, String leadImage, String body, User user, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.leadImage = leadImage;
        this.body = body;
        this.user = user;
        this.date = date;
    }

    @ManyToOne
    @JsonBackReference
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLeadImage() {
        return leadImage;
    }

    public void setLeadImage(String leadImage) {
        this.leadImage = leadImage;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getHoursMinutes() {
        return date.format(DateTimeFormatter.ofPattern("h:mm a"));
    }
}
