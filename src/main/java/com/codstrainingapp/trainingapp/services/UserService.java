package com.codstrainingapp.trainingapp.services;

import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private UsersRepository usersDao;

    @Autowired
    public UserService(UsersRepository usersDao) {
        this.usersDao = usersDao;
    }

    public List<User> findAll() {
        return usersDao.findAll();
    }

    public User findOne(long id) {
        return usersDao.findOne(id);
    }

//    public User findOne(long id) {
//        return (User) usersDao.findById(id);
//    }

    public User findByUsername(String username) {
        return usersDao.findByUsername(username);
    }

    public User findByEmail(String email) {
        return usersDao.findByEmail(email);
    }

    public void save(User user) {
        usersDao.save(user);
    }

    public User update(long id, String field) {
        User updatedUser = usersDao.findOne(id);
        updatedUser.setUsername(field);
        return updatedUser;
    }

    public void delete(User user) {
        usersDao.delete(user);
    }
    public void deleteSession() {
//        SecurityContextHolder.getContext().setAuthentication(null);
        
    }


}