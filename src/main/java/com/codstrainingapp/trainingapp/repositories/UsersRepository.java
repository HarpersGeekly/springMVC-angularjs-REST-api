package com.codstrainingapp.trainingapp.repositories;

import com.codstrainingapp.trainingapp.models.User;

import java.util.List;

public interface UsersRepository {

    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    User findOne(long id);
    void save(User user);
    void update(User user);
    void delete(User user);

//    @Query(nativeQuery = true,
//            value = "SELECT * FROM users WHERE id LIKE=?1")
//    User findById(Long id);
}
