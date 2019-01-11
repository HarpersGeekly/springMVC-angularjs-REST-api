package com.codstrainingapp.trainingapp.services;

import com.codstrainingapp.trainingapp.models.PostVote;
import com.codstrainingapp.trainingapp.repositories.PostVotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostVoteService {

    private PostVotesRepository postVotesDao;

    @Autowired
    public PostVoteService(PostVotesRepository postVotesDao) {
        this.postVotesDao = postVotesDao;
    }

    public void save(PostVote postVote) {
        postVotesDao.save(postVote);
    }

//    int totalPostVote(Long id) {
//        return postVoteDao.totalPostVotes(id);
//    }


}
