package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> findAll(
            @RequestParam(name = "sort", defaultValue = "desc") String sort,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        if (!(sort.equals("desc") || sort.equals("asc"))) {
            throw new IllegalArgumentException();
        }

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException();
        }

        Integer from = page * size;

        return postService.findAll(sort, from, size);
    }

    @PostMapping(value = "/post")
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @GetMapping("/posts/post/{postId}")
    public Post findById(@PathVariable("postId") Integer postId) {
        return postService.findById(postId);
    }
}