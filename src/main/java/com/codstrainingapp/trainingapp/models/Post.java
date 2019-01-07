package com.codstrainingapp.trainingapp.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity // annotation saying "will be a table", an entity in the database
@Table(name = "posts") // name of database table, required for query syntax in Hibernate (org.hibernate.hql.internal.ast.QuerySyntaxException)
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Post {

    @Id // required. Hibernate maps this attribute to a table column named "id". It then maps the following fields automagically
    @GeneratedValue(strategy = GenerationType.IDENTITY) //enable auto ID generation
    private Long id;

    @NotBlank(message = "Title cannot be empty.")
    @Length(min = 5, max = 100, message="Title must be between 5-100 characters.")
    private String title;

    @NotBlank(message = "Subtitle cannot be empty.")
    @Length(min = 5, max = 200, message="Subtitle must be between 5-200 characters.")
    private String subtitle;

    private String leadImage;

    @Column(columnDefinition = "TEXT", length = 50000, nullable = false)
    @NotBlank(message = "Post body cannot be empty.")
    @Length(min = 5, max = 50000, message="Description must be between 5-50000 characters.")
    private String body;

    @Column(name="created_date")
    private LocalDateTime date;

    public Post() {}

    public Post(Long id, String title, String subtitle, String leadImage, String body, User user, LocalDateTime date
//                List<PostVote> votes
    ) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.leadImage = leadImage;
        this.body = body;
        this.user = user;
        this.date = date;
//        this.votes = votes;
    }

    @ManyToOne
    @JsonManagedReference
    private User user;

//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private List<PostVote> votes;

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

//    public List<PostVote> getVotes() {
//        return votes;
//    }
//
//    public void setVotes(List<PostVote> votes) {
//        this.votes = votes;
//    }
//
//    public void addVote(PostVote vote) {
//        votes.add(vote);
//    }
//
//    public void removeVote(PostVote vote) {
//        votes.remove(vote);
//    }



    // MARKDOWN PARSING FOR VIEW ==============================================================

    public String getHtmlTitle() {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(title));
    }

    public String getHtmlLeadImage() {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(leadImage));
    }

    public String getHtmlSubtitle() {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(subtitle));
    }

    public String getHtmlBody() {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(body));
    }


}
