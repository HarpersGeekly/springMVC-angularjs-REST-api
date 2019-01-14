package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.models.ViewModelUser;
import com.codstrainingapp.trainingapp.services.UserService;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @ResponseBody
    public ObjectNode fetchUser(@PathVariable(name="id") long id) {
        User user = userSvc.findOne(id);
        return userSvc.toJson(user);
    }

//---------------------- Update User ---------------------------------------------------

    @PostMapping(value = "/editUser/{id}")
    @ResponseBody// this method currently returns a User as json string.
    public User updateUser(@PathVariable(name = "id") long id, @RequestBody ViewModelUser user, Model viewModel) {
        // User updatedUser = userSvc.findOne(id); // Do not need to find user with /{id} and @PathVariable, user is already provided by @RequestBody User user, which is the converted JSON string back to user object.
        User updatedUser = userSvc.update(user);
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
        redirect.addFlashAttribute("deleteIsSuccessful", true);
        redirect.addFlashAttribute("successMessage", "Sorry to see you go! Your account has been deactivated.");
        return "redirect:/register";
    }

}



