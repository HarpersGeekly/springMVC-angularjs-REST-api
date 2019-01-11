package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.PostVote;

public interface PostVotesRepository {

//    int totalPostVotes(Long id);
    void save(PostVote postVote);
}
