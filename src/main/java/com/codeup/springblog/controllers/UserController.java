package com.codeup.springblog.controllers;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repos.PostRepository;
import com.codeup.springblog.repos.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    private UserRepository users;
    private final PostRepository postDao;
    private final UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository users, PasswordEncoder passwordEncoder, PostRepository postRepository, UserRepository userRepo) {
        this.users = users;
        this.userDao = userRepo;
        this.postDao = postRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/sign-up")
    public String showSignupForm(Model viewModel){
        viewModel.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        users.save(user);
        return "redirect:/login";
    }
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @GetMapping("/users")
    public String show(Model viewModel) {
        return "users/account";
    }
    @GetMapping("/users/{id}/edit")
    public String edit(Model model) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
        System.out.println(getUser.getId());
        model.addAttribute("user", userDao.findById(getUser.getId()));
        return "users/edit";
    }

    @PostMapping("/users/{id}/edit")
    public String update(@PathVariable long id,
                         @RequestParam(name = "username") String username,
                         @RequestParam(name = "email") String email,
                         Model viewModel) {
        User userToBeUpdated = userDao.findOne(id);
        userToBeUpdated.setUsername(username);
        userToBeUpdated.setEmail(email);
        userDao.save(userToBeUpdated);
        return "redirect:/users/"+ userToBeUpdated.getId() + "/edit";
    }

}