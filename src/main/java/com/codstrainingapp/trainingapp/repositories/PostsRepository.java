package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.Post;
import java.util.List;

public interface PostsRepository {

    List<Post> findAllOrderByIdDesc();
    Post findOne(long id);
    void delete(Post post);
    void save(Post post);
}
