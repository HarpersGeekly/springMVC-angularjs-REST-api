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

    public Post(Long id, String title, String subtitle, String leadImage, String body, User user, LocalDateTime date,
                List<PostVote> postVotes
    ) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.leadImage = leadImage;
        this.body = body;
        this.user = user;
        this.date = date;
        this.postVotes = postVotes;
    }

    //=============================== relationships =========================================

    @ManyToOne
    @JsonManagedReference(value = "user_posts")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonBackReference(value = "post_votes")
    private List<PostVote> postVotes;

    //=============================== getters and setters =========================================

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @JsonGetter("hoursMinutes")
    public String hoursMinutes() {
        return date.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    @JsonGetter("formatDate")
    public String formatDate() {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public List<PostVote> getPostVotes() {
        return postVotes;
    }

    public void setVotes(List<PostVote> postVotes) {
        this.postVotes = postVotes;
    }

    // VOTING LOGIC =============================================================================
    @JsonGetter("voteCount") // saying that this method is only being used as an attribute in show.html
    public int voteCount() {
        return postVotes.stream().mapToInt(PostVote::getType).reduce(0, (sum, vote) -> sum + vote);
        // takes all the votes and adds 1 or -1 (getType). Needs more users in application to vote and see results.
        // http://www.baeldung.com/java-8-double-colon-operator (::)
        // stream(), mapToInt(), reduce()
        // https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html
        // A stream represents a sequence of elements and supports different kinds of operations to perform computations upon those elements.
        // Streams can be created from various data sources, especially collections. Lists and Sets support new methods stream()
        // mapToInt() returns an IntStream consisting of the results of applying the given function to the elements of this stream.
        // PostVote::getType will evaluate to a function that invokes getType() directly without any delegation.
        // There’s a really tiny performance difference due to saving one level of delegation.
        // reduce() sums the values
    }

    public PostVote getVoteFrom(User user) {
        for (PostVote vote : postVotes) {
            if (vote.voteBelongsTo(user)) {
                return vote;
            }
        }
        return null;
    }

    public void addVote(PostVote vote) {
        postVotes.add(vote);
    }

    public void removeVote(PostVote vote) {
        postVotes.remove(vote);
    }



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
