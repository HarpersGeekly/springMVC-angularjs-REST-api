package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Password;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.repositories.ListUsersDao;
import com.codstrainingapp.trainingapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
//@SessionAttributes("user")
public class UsersController {

//    private UsersRepository usersDao;
    private ListUsersDao usersDao = new ListUsersDao();

//    @Autowired
//    public UsersController(UsersRepository usersDao) {
//        this.usersDao = usersDao;
//    }

    @GetMapping("/login")
    public String showLoginForm(Model viewModel, HttpServletRequest request) {
        System.out.println("got to login GET method");

        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return "redirect:/profile";
        }

        List<User> users = usersDao.findAll();
        for (User u : users) {
            System.out.println("current users: " + u.getUsername() + ", password: " + u.getPassword());
        }

        viewModel.addAttribute("message", "Welcome to the login page - TEST");
        viewModel.addAttribute("user", new User());
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model viewModel, HttpServletRequest request, RedirectAttributes redirect) {


        User existingUser = usersDao.findByUsername(user.getUsername());

        System.out.println("got to login POST method");
        System.out.println(existingUser.getId());
        System.out.println(existingUser.getUsername());
        System.out.println(existingUser.getPassword());

        boolean usernameIsEmpty = user.getUsername() == null;
        boolean passwordIsEmpty = user.getPassword() == null;
        boolean passwordsMatch = existingUser.getPassword().equals(user.getPassword());

//
        boolean validAttempt = existingUser.getUsername().equals(user.getUsername()) && existingUser.getPassword().equals(user.getPassword());
            if(!validAttempt || usernameIsEmpty || passwordIsEmpty || !passwordsMatch) {
                System.out.println("not a valid attempt");
                viewModel.addAttribute("errorMessage", true);
                viewModel.addAttribute("user", user);
                return "users/login";
            }

        request.getSession().setAttribute("user", existingUser);
        redirect.addFlashAttribute("user", existingUser);
        return "redirect:/profile/" + existingUser.getId() + "/" + existingUser.getUsername();
    }

    @GetMapping("/register")
    public String register(Model viewModel) {

        List<User> users = usersDao.findAll();
        for (User u : users) {
            System.out.println("user:" + u.getUsername());
        }

        viewModel.addAttribute("message", "Welcome to the register page - TEST");
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam(name = "username") String username,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password,
                           HttpServletRequest request) {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setDate(LocalDateTime.now());

        usersDao.insert(user);

        System.out.println(user.getId());
        System.out.println(user.getUsername());
//        User existingUser = usersDao.findByUsername(user.getUsername());

//        if (existingUser != null) {
        //if there is already a user in the database...
//        }

//        usersDao.save(user);
//        return "/login";
//        userSvc.authenticate(user); //automatically login user
        request.getSession().setAttribute("user", user);


//        long id = user.getId();
//        String newUserUsername = user.getUsername();
        return "redirect:/profile/" + user.getId() + '/' + user.getUsername();
//        return "redirect:/profile";
    }

    @GetMapping("/profile/{id}/{username}")
    public String showOtherUsersProfile(@PathVariable long id, Model viewModel) {
        User user = usersDao.findOne(id);
        viewModel.addAttribute("message", "Welcome to the profile page - TEST");
        viewModel.addAttribute("user", user);
        System.out.println("user: " + user.getUsername());

//        old way
//        User u = (User)request.getSession().getAttribute("user");
//        System.out.println(u.getUsername());
//        viewModel.addAttribute("user", u.getUsername());
        return "users/profile";
    }

    @GetMapping("/profile")
    public String showProfile(HttpServletRequest request) {

        User sessionUser = (User)request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        User user = usersDao.findOne(sessionUser.getId());


        return "redirect:/profile" + user.getId() + '/' + user.getUsername();
    }

}


