package com.codstrainingapp.trainingapp.services;

import com.codstrainingapp.trainingapp.models.Post;
import com.codstrainingapp.trainingapp.models.PostDTO;
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

    public Post findOne(Long id) {
        return postsDao.findOne(id);
    }

    public List<Post> findAllByUserId(Long id) {
        return postsDao.findAllByUserId(id);
    }

    public PostDTO savePost(PostDTO post) {
        Post entity = convertToPost(post);
        postsDao.savePost(entity);
        return convertToPostDTO(entity);
    }

    public Post updatePost(Post post) {
        Post existingPost = postsDao.findOne(post.getId());
        existingPost.setTitle(post.getTitle());
        existingPost.setSubtitle(post.getSubtitle());
        existingPost.setLeadImage(post.getLeadImage());
        existingPost.setBody(post.getBody());
        return existingPost;
    }

    public void delete(Post post) {
        postsDao.delete(post);
    }

    private PostDTO convertToPostDTO(Post post){
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSubtitle(post.getSubtitle());
        dto.setLeadImage(post.getLeadImage());
        dto.setBody(post.getBody());
        dto.setDate(post.getDate());
        dto.setUser(post.getUser());
        dto.setVotes(post.getPostVotes());
        return dto;
    }

    private Post convertToPost(PostDTO dto){
        Post entity = new Post();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setSubtitle(dto.getSubtitle());
        entity.setBody(dto.getBody());
        entity.setLeadImage(dto.getLeadImage());
        entity.setDate(dto.getDate());
        entity.setUser(dto.getUser());
        entity.setPostVotes(dto.getPostVotes());
        return entity;
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
