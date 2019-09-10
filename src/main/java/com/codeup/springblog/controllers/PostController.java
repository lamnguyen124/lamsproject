package com.codeup.springblog.controllers;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.repos.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

    @Controller
    public class PostController {

        private final PostRepository postDao;

        public PostController(PostRepository postRepository) {
            postDao = postRepository;
        }

        @GetMapping("/posts")
        public String index(Model vModel) {
            Iterable<Post> posts = postDao.findAll();
            vModel.addAttribute("posts", posts);
            return "posts/index";
        }

        @GetMapping("/posts/{id}")
        public String show(@PathVariable long id, Model viewModel) {
            Post post = postDao.findOne(id);
            viewModel.addAttribute("post", post);
            return "posts/show";
        }

        @GetMapping("/posts/search/{term}")
        public String show(@PathVariable String term, Model viewModel) {
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

        @GetMapping("posts/create")
        public String showCreateForm() {
            return "posts/create";
        }

        @PostMapping("/posts/create")
        public String createPost(
                @RequestParam(name = "title") String titleParam,
                @RequestParam(name = "description") String descParam
        ) {
            Post postToBeCreated = new Post();
            postToBeCreated.setTitle(titleParam);
            postToBeCreated.setDescription(descParam);
            Post savedPost = postDao.save(postToBeCreated);
            return "redirect:/posts/" + savedPost.getId();
        }
    }





