package com.codeup.springblog.controllers;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.repos.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    }


//    //    GET	/posts/create	view the form for creating a post
//    @GetMapping("posts/view")
//    public String createPostForm() {
//        return "/posts/index";
//    }
//
//
//    //    POST	/posts/create	create a new post
//    @PostMapping("posts/create")
//    public String createPost() {
//        return "posts/create";
//    }
//
//
//    @GetMapping("/index")
//    public String welcome() {
//        return "posts/index";
//    }
//
//    @GetMapping("/show")
//    public String show() {
//        return "posts/show";
//    }
//}
//

