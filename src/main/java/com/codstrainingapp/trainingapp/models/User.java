package com.codstrainingapp.trainingapp.models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//JPA specification defines an object-relational mapping between tables in a relational database and a set of Java classes.
@Entity(name="users") //is a POJO with mapping information. It's now a jpa entity object. Attributes then get automatically mapped to database columns with the same name
public class User {

    @Id // identifier, maps the primary key column
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "(?=^.{3,20}$)^[a-zA-Z][a-zA-Z0-9 ]*[._-]?[a-zA-Z0-9 ]+$", message = "Username must be alphanumeric only.")
    @NotBlank(message="Please enter a username.")
    @Length(min = 2, max = 20, message="Your username must be between 2-20 characters.")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Please enter an email address.")
    @Email(message = "That email is not a valid email address.")
    private String email;

    @Length(max = 1500, message="Your bio must be less than 1500 characters.")
    private String bio;

    @Column(nullable = false)
    @NotBlank(message = "Your password cannot be empty.")
    @Length(min = 8, max = 100, message="Your password must be between 8-100 characters.") // BCrypt PasswordEncoder hashes passwords with 60 random characters. Make sure the max is >= 60
    @JsonIgnore //password is hidden from the client
    private String password;

    @Column(name = "joined_date")
    private LocalDateTime date;

    public User(){}

    public User(Long id, String username, String email, String bio, String password, LocalDateTime date) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.password = password;
        this.date = date;
    }

    public User(String username, String email, String bio) {
        this.username = username;
        this.email = email;
        this.bio = bio;
    }

//    ============================= relationships ==========================

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference(value = "user_posts")
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference(value = "user_post_votes")
    private List<PostVote> postVotes;

//    ============================ getters and setters =====================

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
//        this.password = Password.hash(password);
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<PostVote> getPostVotes() {
        return postVotes;
    }

    public void setVotes(List<PostVote> votes) {
        this.postVotes = postVotes;
    }

    @JsonGetter("hoursMinutes")
    public String hoursMinutes() {
        return date.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    @JsonGetter("formatDate")
    public String formatDate() {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
}
