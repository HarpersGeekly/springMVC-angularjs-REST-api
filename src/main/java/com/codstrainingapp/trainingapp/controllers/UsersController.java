package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Password;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.services.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class UsersController {

    private UserService userSvc;

    @Autowired
    public UsersController(UserService userSvc) {
        this.userSvc = userSvc;
    }

//------------------------------------------- Login -------------------------------------------------

    @GetMapping("/login")
    public String showLoginForm(Model viewModel, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return "redirect:/profile";
        }
        viewModel.addAttribute("message", "Welcome to the login page - TEST");
        viewModel.addAttribute("user", new User());
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model viewModel, HttpServletRequest request, RedirectAttributes redirect) {
        boolean usernameIsEmpty = user.getUsername().isEmpty();
        boolean passwordIsEmpty = user.getPassword().isEmpty();
        User existingUser = userSvc.findByUsername(user.getUsername());
        boolean userExists = (existingUser != null);
        
        boolean validAttempt = userExists && existingUser.getUsername().equals(user.getUsername()) && Password.check(user.getPassword(), existingUser.getPassword());

        if (userExists && !passwordIsEmpty) {
            boolean passwordCorrect =  Password.check(user.getPassword(), existingUser.getPassword()); // check the submitted password against what I have in the database
            // incorrect password:
            if (!passwordCorrect) {
                viewModel.addAttribute("passwordIsIncorrect", true);
                viewModel.addAttribute("user", user);
                return "users/login";
            }
        }

        // both empty...
        if (usernameIsEmpty && passwordIsEmpty) {
            viewModel.addAttribute("usernameIsEmpty", true);
            viewModel.addAttribute("passwordIsEmpty", true);
            return "users/login";
        }
        //one or the other...
        if (!usernameIsEmpty && passwordIsEmpty) {
            viewModel.addAttribute("passwordIsEmpty", true);
            viewModel.addAttribute("user", user);
            return "users/login";
        }
        if (usernameIsEmpty && !passwordIsEmpty) {
            viewModel.addAttribute("usernameIsEmpty", true);
            viewModel.addAttribute("user", user);
            return "users/login";
        }

        // username doesn't exist:
        if ((!usernameIsEmpty && !passwordIsEmpty) && !userExists) {
            request.setAttribute("userNotExist", !userExists);
            return "users/login";
        }

        if (!validAttempt) {
            System.out.println("not a valid attempt");
            viewModel.addAttribute("errorMessage", true);
            viewModel.addAttribute("user", user);
            return "users/login";
        } else {
            request.getSession().setAttribute("user", existingUser);
            redirect.addFlashAttribute("user", existingUser);
            return "redirect:/profile/" + existingUser.getId() + "/" + existingUser.getUsername();
        }
    }

//-------------------------------------- Register --------------------------------------------------

    @GetMapping("/register")
    public String register(Model viewModel, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return "redirect:/profile";
        }
        viewModel.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(
//                           @RequestParam(name = "username") String username,
//                           @RequestParam(name = "email") String email,
//                           @RequestParam(name = "password") String password,
//                           @RequestParam(name = "password_confirm") String passwordConfirmation,
//                           HttpServletRequest request) {
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model viewModel,
            HttpServletRequest request,
            @RequestParam(name = "confirm_password") String passwordConfirmation) {

        if (user.getBio().isEmpty()) {
            user.setBio(null);
        } else {
            user.setBio(user.getBio());
        }

        User existingUser = userSvc.findByUsername(user.getUsername());
        if (existingUser != null) {
            result.rejectValue(
                    "username",
                    "user.username",
                    "Username is already taken.");
        }

        User existingEmail = userSvc.findByEmail(user.getEmail());
        if (existingEmail != null) {
            result.rejectValue(
                    "email",
                    "user.email",
                    "Email is already used.");
        }

        //compare passwords:
        if (!passwordConfirmation.equals(user.getPassword())) {
            result.rejectValue(
                    "password",
                    "user.password",
                    "Your passwords do not match.");
        }

        // if there are errors, show the form again.
        if (result.hasErrors()) {
            result.getFieldErrors().stream().forEach(f -> System.out.println((f.getField() + ": " + f.getDefaultMessage())));
            System.out.println("errors");
            viewModel.addAttribute("errors", result);
            viewModel.addAttribute("user", user);
            return "users/register";
        }

        //TODO need to set to null???
        user.setPosts(null);
        user.setPassword(Password.hash(user.getPassword()));
        user.setDate(LocalDateTime.now());
        userSvc.save(user);
        request.getSession().setAttribute("user", user);
        return "redirect:/profile/" + user.getId() + '/' + user.getUsername();
    }

// ---------------------------- Profile -------------------------------------------------------

    @GetMapping("/profile/{id}/{username}")
    public String showOtherUsersProfile(@PathVariable long id, Model viewModel) {
        User user = userSvc.findOne(id);
        viewModel.addAttribute("user", user);
        System.out.println(user);
        Gson gson = new Gson();

        //TODO this is the issue: error: org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.codstrainingapp.trainingapp.models.User.posts, could not initialize proxy - no Session
        String userJson = gson.toJson(user);
        System.out.println(userJson);
        viewModel.addAttribute("userJson", userJson);
        return "users/profile";
    }

    @GetMapping("/profile")
    public String showProfile(HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        User user = userSvc.findOne(sessionUser.getId());
        return "redirect:/profile/" + user.getId() + '/' + user.getUsername();
    }

//----------------------- Get User -------------------------------------------------------

    @GetMapping(value = "/getUser/{id}", produces = "application/json")
    @ResponseBody
    public String getUser(@PathVariable(name="id") long id) {
        Gson gson = new Gson();
        return gson.toJson(userSvc.findOne(id));
}

//---------------------- Update User ---------------------------------------------------

    @PostMapping("/editUser/{id}")
    @ResponseBody
    public User updateUser(@PathVariable("id") long id, @RequestBody User user, Model viewModel) {
//        User updatedUser = userSvc.findOne(id); //Do not need to find user, user is already provided by @RequestBody User user, which is the converted JSON string back to user object.
        User updatedUser = userSvc.update(id, user.getUsername());
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


