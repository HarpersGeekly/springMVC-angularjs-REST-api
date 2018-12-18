package com.codstrainingapp.trainingapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message="Please enter a username.")
    @Length(min = 2, max = 20, message="Your username must be between 2-20 characters.")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Please enter an email address.")
    @Email(message = "That email is not a valid email address.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Your password cannot be empty.")
    @Length(min = 8, max = 100, message="Your password must be between 8-100 characters.") // BCrypt PasswordEncoder hashes passwords with 60 random characters. Make sure the max is >= 60
    @JsonIgnore //password is hidden from the client
    private String password;

    @Column(name = "joined_date")
    private LocalDateTime date;

    public User(){}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(long id, String username, String email, String password, LocalDateTime date) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.date = date;
    }

//    ============================= relationships ==========================

//    @OneToMany
//    public List<Post> posts;

//    ============================ getters and setters =====================

    public long getId() {
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
}
