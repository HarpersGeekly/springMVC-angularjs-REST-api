package com.codstrainingapp.trainingapp.services;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.repositories.PostsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
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

    public List<Post> findAllOrderByIdDesc() {
        return postsDao.findAllOrderByIdDesc();
    }

    public Post findOne(long id) {
        return postsDao.findOne(id);
    }

    public void delete(Post post) {
        postsDao.delete(post);
    }

    public void save(Post post) {
        postsDao.save(post);
    }

    public String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public Object fromJson(String jsonString, Object valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, (JavaType) valueType);
    }
}
