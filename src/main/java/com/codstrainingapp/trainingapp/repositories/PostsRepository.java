package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.Post;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository {

    List<Post> findAll();
    List<Post> findAllOrderByIdDesc();
    List<Post> findAllByUserId(long id);
    Post findOne(long id);
    void delete(Post post);
    void savePost(Post post);
    void updatePost(Post post);
}
