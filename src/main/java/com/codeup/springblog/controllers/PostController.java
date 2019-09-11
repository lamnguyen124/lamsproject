package com.codeup.springblog.controllers;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repos.PostRepository;
import com.codeup.springblog.repos.UserRepository;
import com.codeup.springblog.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private final PostRepository postDao;
    private final UserRepository userDao;

    public PostController(PostRepository postRepository, UserRepository userRepo){
        this.userDao = userRepo;
        this.postDao = postRepository;
    }

    @Autowired
    private EmailService emailService;

    @GetMapping("/posts")
    public String index(Model vModel) {
        Iterable<Post> posts = postDao.findAll();
        vModel.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/ads/{id}")
    public String show(@PathVariable long id, Model viewModel) {
        Post post = postDao.findOne(id);
        viewModel.addAttribute("post", post);
        return "posts/show";
    }

    @GetMapping("/posts/search")
    public String show(@RequestParam(name = "term") String term, Model viewModel) {
        List<Post> posts = postDao.searchByTitleLike(term);
        viewModel.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/posts/{id}/edit")
    public String edit(@PathVariable long id, Model viewModel) {
        Post post = postDao.findOne(id);
        viewModel.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String update(@PathVariable long id,
                         @RequestParam(name = "title") String title,
                         @RequestParam(name = "description") String description,
                         Model viewModel) {
        Post postToBeUpdated = postDao.findOne(id);
        postToBeUpdated.setTitle(title);
        postToBeUpdated.setDescription(description);
        postDao.save(postToBeUpdated);
        return "redirect:/posts/" + postToBeUpdated.getId();
    }

    @PostMapping("/posts/{id}/delete")
    public String delete(@PathVariable long id){
        postDao.delete(id);
        return "redirect:/posts";
    }

    @GetMapping("/posts/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    @PostMapping("/posts/create")
    public String createPost(
            @ModelAttribute Post postPassedIn
    ) {
        User userDB = userDao.findOne(1L);
        postPassedIn.setUser(userDB);

        Post savedPost = postDao.save(postPassedIn);
        emailService.prepareAndSend(
                savedPost,
                "Post created",
                String.format("Post with the id %d has been created", savedPost.getId()));
        return "redirect:/posts/" + savedPost.getId();
    }
}