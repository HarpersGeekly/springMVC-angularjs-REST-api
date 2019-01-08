package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.Post;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PostsRepositoryImpl extends AbstractDao<Long, Post> implements PostsRepository {

    @SuppressWarnings("unchecked")
    public List<Post> findAll() {
        Criteria criteria = createEntityCriteria();
        List<Post> posts = (List<Post>) criteria.list();
        for(Post p : posts) {
            Hibernate.initialize(p.getPostVotes());
        }
        return posts;
    }

    @SuppressWarnings("unchecked")
    public List<Post> findAllOrderByIdDesc() {
        Query query = createCustomQuery("FROM Post ORDER BY id DESC");
        query.setMaxResults(3);
        return query.list();
    }

    public Post findPost(long id) {
        return getByKey(id);
    }

//    @SuppressWarnings("unchecked")
//    public List<Post> latestPosts() {
//        Criteria criteria = createEntityCriteria();
//        return (List<Post>) criteria.list();
//    }

    public Post findOne(long id) {
        return getByKey(id);
    }

    public void save(Post post) {
        persist(post);
    }
}


