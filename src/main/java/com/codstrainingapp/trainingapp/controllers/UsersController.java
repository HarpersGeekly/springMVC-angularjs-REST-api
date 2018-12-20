package com.codstrainingapp.trainingapp.controllers;

import com.codstrainingapp.trainingapp.models.Password;
import com.codstrainingapp.trainingapp.models.User;
import com.codstrainingapp.trainingapp.repositories.ListUsersDao;
import com.codstrainingapp.trainingapp.repositories.UsersRepository;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
//@SessionAttributes("user")
public class UsersController {

    //TODO
    //private UsersRepository usersDao;
    private ListUsersDao usersDao = new ListUsersDao();

    //TODO
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

        boolean usernameIsEmpty = user.getUsername().isEmpty();
        boolean passwordIsEmpty = user.getPassword().isEmpty();
        System.out.println(user.getUsername());
        System.out.println(usernameIsEmpty);
        System.out.println(passwordIsEmpty);

        User existingUser = usersDao.findByUsername(user.getUsername());
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

    @GetMapping("/register")
    public String register(Model viewModel) {

        List<User> users = usersDao.findAll();
        for (User u : users) {
            System.out.println("user:" + u.getUsername());
        }

        viewModel.addAttribute("message", "Welcome to the register page - TEST");
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
            @Valid User user,
            BindingResult result,
            Model viewModel,
            HttpServletRequest request,
            @RequestParam(name = "confirm_password") String passwordConfirmation) {

        User existingUser = usersDao.findByUsername(user.getUsername());
        if (existingUser != null) {
            result.rejectValue(
                    "username",
                    "user.username",
                    "Username is already taken.");
        }

        User existingEmail = usersDao.findByEmail(user.getEmail());
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
            viewModel.addAttribute("errors", result);
            viewModel.addAttribute("user", user);
            return "users/register";
        }

        user.setDate(LocalDateTime.now());
        usersDao.insert(user);
        request.getSession().setAttribute("user", user);
        return "redirect:/profile/" + user.getId() + '/' + user.getUsername();
    }

    @GetMapping("/profile/{id}/{username}")
    public String showOtherUsersProfile(@PathVariable long id, Model viewModel) {
        User user = usersDao.findOne(id);
        viewModel.addAttribute("user", user);
        return "users/profile";
    }

    @GetMapping("/profile")
    public String showProfile(HttpServletRequest request) {

        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        User user = usersDao.findOne(sessionUser.getId());
        return "redirect:/profile/" + user.getId() + '/' + user.getUsername();
    }

    @GetMapping("/getUser/{id}")
    @ResponseBody
    public User getUser(@PathVariable(name="id") long id) {
        return usersDao.findOne(id);
}

//------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/editUser/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        System.out.println("Updating User " + id);

        User currentUser = usersDao.findOne(id);

        if (currentUser==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        usersDao.update(currentUser);

        List<User> users = usersDao.findAll();
        for(User u : users) {
            System.out.println(u.getUsername());
        }
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }


}


