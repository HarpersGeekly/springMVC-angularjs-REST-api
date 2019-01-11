package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.PostVote;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PostVotesRepositoryImpl extends AbstractDao<Long, PostVote> implements PostVotesRepository {

    public void save(PostVote postVote) {
        persist(postVote);
    }


//    public int totalPostVotes(Long id) {
//        Query query = createCustomQuery("FROM PostVote");
//        return query.getFetchSize();
//    }
}
