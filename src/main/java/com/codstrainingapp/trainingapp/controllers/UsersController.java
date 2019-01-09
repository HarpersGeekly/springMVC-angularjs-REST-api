package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.services.UserService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UsersController {

    private UserService userSvc;

    @Autowired
    public UsersController(UserService userSvc) {
        this.userSvc = userSvc;
    }

// ---------------------------- Profile -------------------------------------------------------

    @GetMapping("/profile")
    public String showProfile(HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        User user = userSvc.findOne(sessionUser.getId());
        return "redirect:/profile/" + user.getId() + '/' + user.getUsername();
    }

    @GetMapping("/profile/{id}/{username}")
    public String showOtherUsersProfile(@PathVariable long id, Model viewModel) {
        User user = userSvc.findOne(id);
        viewModel.addAttribute("user", user);
        return "users/profile";
    }

//----------------------- Get User -------------------------------------------------------

    @GetMapping(value = "/getUser/{id}")
    @ResponseBody // this method currently returns a User as json string.
    public ObjectNode fetchUser(@PathVariable(name="id") long id) throws JsonProcessingException {
        User user = userSvc.findOne(id);
        return userSvc.toJson(user);
    }

//---------------------- Update User ---------------------------------------------------

    @PostMapping("/editUser/{id}")
    @ResponseBody
    public User updateUser(@PathVariable("id") long id, @RequestBody User user, Model viewModel) {
//        User updatedUser = userSvc.findOne(id); // Do not need to find user, user is already provided by @RequestBody User user, which is the converted JSON string back to user object.
        User updatedUser = userSvc.update(id, user.getUsername(), user.getEmail(), user.getBio());
        viewModel.addAttribute("user", updatedUser);
        return updatedUser;
    }

//--------------------- Delete User ----------------------------------------------------

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") long id, HttpServletRequest request, RedirectAttributes redirect, Model viewModel) {
        User user = userSvc.findOne(id);
        userSvc.delete(user);
        request.getSession().removeAttribute("user");
        request.getSession().invalidate();
        redirect.addFlashAttribute("successDelete", userSvc.findOne(id) == null);
        redirect.addFlashAttribute("successMessage", "Sorry to see you go! Your account has been deactivated.");
        return "redirect:/register";
    }

}



