package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.models.UserDTO;
import com.codstrainingapp.trainingapp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController // includes @ResponseBody
@RequestMapping("/api/user")
@CrossOrigin
public class UsersRestController {

    private UserService userSvc;

    @Autowired
    public UsersRestController(UserService userSvc) {
        this.userSvc = userSvc;
    }

    @GetMapping(value = "/users")
    public List<UserDTO> findAll() {
        return userSvc.findAll();
    }

    @GetMapping(value = "/id/{id}")
    public UserDTO findById(@PathVariable(name = "id") Long id) {
        return userSvc.findOne(id);
    }

    @GetMapping(value = "/username/{username}")
    public UserDTO findByUsername(@PathVariable(name = "username") String username) {
        return userSvc.findByUsername(username);
    }

    @GetMapping(value = "/email/{email}")
    public UserDTO findByEmail(@PathVariable(name = "email") String email) {
        return userSvc.findByEmail(email);
    }

//---------------------- Save User ------------------------------------------------------

    @PostMapping(value = "/saveUser")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO saveUser(@RequestBody UserDTO user) {
        return userSvc.saveUser(user);
    }

//---------------------- Update User ---------------------------------------------------

    @PutMapping(value = "/editUser")
    public UserDTO updateUser(@RequestBody UserDTO user) {
        return userSvc.updateUser(user);
    }

//--------------------- Delete User ----------------------------------------------------

    @DeleteMapping(value = "/deleteUser/{id}")
    public void deleteUser(@PathVariable Long id) {
        UserDTO user = userSvc.findOne(id);
        userSvc.delete(user);
    }

}








//    //-------------------Retrieve Single User--------------------------------------------------------
//
//    @RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
//        System.out.println("Fetching User with id " + id);
//        User user = userSvc.findOne(id);
//        if (user == null) {
//            System.out.println("User with id " + id + " not found");
//            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<User>(user, HttpStatus.OK);
//    }
//
// IS THE SAME AS...
//----------------------- Get User -------------------------------------------------------

//    @Deprecated
//    @GetMapping(value = "/getUser/{id}")
//    public ObjectNode fetchUserToJson(@PathVariable(name="id") long id) {
//        User user = userSvc.findOne(id);
//        return userSvc.toJson(user);
//    }

//    @GetMapping(value = "/getUser/{id}")
//    public User fetchUser(@PathVariable(name = "id") long id) {
//        return userSvc.findOne(id);
//    }

