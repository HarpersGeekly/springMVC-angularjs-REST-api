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
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
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

    public User(Long id, String username, String email, String bio, String password, LocalDateTime date, List<Post> posts,
                List<PostVote> postVotes
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.password = password;
        this.date = date;
        this.posts = posts;
        this.postVotes = postVotes;
    }

    public User(String username, String email, String bio) {
        this.username = username;
        this.email = email;
        this.bio = bio;
    }

//    ============================= relationships ==========================

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Post> posts;

//    The default fetch type for one-to-many relationship is LAZY. FetchType.LAZY is a hint to the JPA runtime,
//    indicating that you want to defer loading of the field until you access it. This is called lazy loading.
//    Lazy loading is completely transparent; data is loaded from the database in objects silently when you attempt
//    to read the field for the first time. The other possible fetch type is FetchType.EAGER. Whenever you retrieve
//    an entity from a query or from the EntityManager, you are guaranteed that all of its eager fields are populated
//    with data store data. In order to override the default fetch type, EAGER fetching has been specified
//    with fetch=FetchType.EAGER.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
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

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public String getHoursMinutes() {
        return date.format(DateTimeFormatter.ofPattern("h:mm a"));
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
}
