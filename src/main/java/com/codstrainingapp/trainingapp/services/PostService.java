package com.codstrainingapp.trainingapp.services;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PostService {

    private PostsRepository postsDao;

    @Autowired
    public PostService(PostsRepository postsDao) {
        this.postsDao = postsDao;
    }

    public List<Post> findAll() {
       return postsDao.findAll();
    }

    public Post findOne(long id) {
        return postsDao.findOne(id);
    }
}
