package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.Post;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostsRepositoryImpl extends AbstractDao<Long, Post> implements PostsRepository {

    @SuppressWarnings("unchecked")
    public List<Post> findAll() {
        Criteria criteria = createEntityCriteria();
        return (List<Post>) criteria.list();
    }
}


