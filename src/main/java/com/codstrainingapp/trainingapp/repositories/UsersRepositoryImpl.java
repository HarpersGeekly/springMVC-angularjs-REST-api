package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.User;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("usersRepository")
public class UsersRepositoryImpl extends AbstractDao<Long, User> implements UsersRepository {

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        Criteria criteria = createEntityCriteria();
        List<User> users = (List<User>) criteria.list();
        for(User u : users) {
            Hibernate.initialize(u.getPosts());
        }
        return users;
    }

    public User findByUsername(String username) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("username", username));
        return (User) criteria.uniqueResult();
    }

    public User findByEmail(String email) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("email", email));
        return (User) criteria.uniqueResult();
    }

    public User findOne(long id) {
        User user = getByKey(id);
        Hibernate.initialize(user.getPosts());
        Hibernate.initialize(user.getPostVotes());
        return user;
    }

    public void save(User user) {
        persist(user);
    }


    public int totalPostVotes(Long id) {
        Query query = createCustomQuery("FROM PostVote pv LEFT JOIN Post p ON p.id = pv.post_id inner JOIN User u ON u.id = p.user_id WHERE u.id LIKE ?1");
        return query.getFetchSize();
    }

}
