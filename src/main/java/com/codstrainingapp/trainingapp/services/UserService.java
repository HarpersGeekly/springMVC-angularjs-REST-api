package com.codstrainingapp.trainingapp.services;

import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.models.UserDTO;
import com.codstrainingapp.trainingapp.repositories.UsersRepository;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    public User findOne(Long id) {
        return usersDao.findOne(id);
    }

    public User findByUsername(String username) {
        return usersDao.findByUsername(username);
    }

    public User findByEmail(String email) {
        return usersDao.findByEmail(email);
    }

    public UserDTO saveUser(UserDTO user) {
        User entity = convertToUser(user);
        usersDao.saveUser(entity);
        return convertToUserDTO(entity);
    }

    public User updateUser(User user) {
        User existingUser = usersDao.findOne(user.getId());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setBio(user.getBio());
        return existingUser;
    }

    public void delete(User user) {
        usersDao.delete(user);
    }

    private UserDTO convertToUserDTO(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setBio(user.getBio());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setDate(user.getDate());
        System.out.println("dto id: " + dto.getId());
        return dto;
    }

    private User convertToUser(UserDTO userDto){
        User entity = new User();
        entity.setId(userDto.getId());
        entity.setUsername(userDto.getUsername());
        entity.setEmail(userDto.getEmail());
        entity.setBio(userDto.getBio());
        entity.setPassword(userDto.getPassword());
        entity.setDate(userDto.getDate());
        return entity;
    }

    public ObjectNode toJson(User user) {
//        List<Post> posts = user.getPosts();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userNode = mapper.valueToTree(user);
//        ArrayNode postArray = mapper.valueToTree(posts);
//        userNode.putArray("posts").addAll(postArray);
        JsonNode result = mapper.createObjectNode().set("user", userNode);
        return (ObjectNode) result;
    }

    public Object fromJson(String jsonString, Object valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, (JavaType) valueType);
    }


}