package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.Comment;

import java.util.List;

public interface CommentsRepository {

    Comment findOne(long id);
    List findAllByPostId(long id);
    List<Comment> findAllByUserId(long id);
    void saveComment(Comment comment);
}
