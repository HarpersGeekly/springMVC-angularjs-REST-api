package com.codstrainingapp.trainingapp.controllers;

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
import java.util.List;

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
        System.out.println("got to login GET method");

        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return "redirect:/profile";
        }

        List<User> users = userSvc.findAll();
        for (User u : users) {
            System.out.println("current users: " + u.getUsername() + ", password: " + u.getPassword());
        }

        viewModel.addAttribute("message", "Welcome to the login page - TEST");
        viewModel.addAttribute("user", new User());
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model viewModel, HttpServletRequest request, RedirectAttributes redirect) {

        boolean usernameIsEmpty = user.getUsername().isEmpty();
        boolean passwordIsEmpty = user.getPassword().isEmpty();
        System.out.println(user.getUsername());
        System.out.println(usernameIsEmpty);
        System.out.println(passwordIsEmpty);

        User existingUser = userSvc.findByUsername(user.getUsername());
        System.out.println("got to login POST method");

        boolean userExists = (existingUser != null);
        boolean validAttempt = userExists && existingUser.getUsername().equals(user.getUsername()) && existingUser.getPassword().equals(user.getPassword());

        if (userExists && !passwordIsEmpty) {
            boolean passwordCorrect = existingUser.getPassword().equals(user.getPassword()); // check the submitted password against what I have in the database
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

        List<User> users = userSvc.findAll();
        for (User u : users) {
            System.out.println("user:" + u.getUsername());
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

        System.out.println("bio:" + user.getBio());

        if (user.getBio().isEmpty()) {
            user.setBio(null);
        } else {
            user.setBio(user.getBio());
        }

        System.out.println(user.getBio());

        System.out.println("get to register post");
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

        user.setDate(LocalDateTime.now());
        userSvc.save(user);
        request.getSession().setAttribute("user", user);

        List<User> users = userSvc.findAll();
        for (User u : users) {
            System.out.println("user:" + u.getUsername());
        }
        return "redirect:/profile/" + user.getId() + '/' + user.getUsername();
    }

// ---------------------------- Profile -------------------------------------------------------

    @GetMapping("/profile/{id}/{username}")
    public String showOtherUsersProfile(@PathVariable long id, Model viewModel) {
        User user = userSvc.findOne(id);
        viewModel.addAttribute("user", user);

        System.out.println("===GOT TO PROFILE===");
        System.out.println(user.getUsername());

        Gson gson = new Gson();
        String userJson = gson.toJson(user);

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
        System.out.println(gson.toJson(userSvc.findOne(id)));
        return gson.toJson(userSvc.findOne(id));
}

//---------------------- Update User ---------------------------------------------------

    @PostMapping("/editUser/{id}")
    @ResponseBody
    public User updateUser(@PathVariable("id") long id, @RequestBody User user, Model viewModel) {
        User updatedUser = userSvc.findOne(id);

        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());

        userSvc.update(updatedUser);

        List<User> users = userSvc.findAll();
        for(User u : users) {
            System.out.println(u.getUsername());
            System.out.println(u.getEmail());
        }

        viewModel.addAttribute("user", updatedUser);
        return updatedUser;
    }


}


