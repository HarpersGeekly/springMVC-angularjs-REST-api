package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("usersRepository")
public class UsersRepositoryImpl extends AbstractDao<Long, User> implements UsersRepository {

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        Criteria criteria = createEntityCriteria();
        return (List<User>) criteria.list();
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
        return getByKey(id);
    }

    public void save(User user) {
        persist(user);
    }

    public void update(User user) {
        update(user);
    }

//    public User findById(Long id) {
//        return findOne(id);
//    }


}
