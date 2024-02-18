package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.PostNotFoundException;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>();
    private final UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(String sort, Integer from, Integer size) {
        Comparator<Post> comparator = Comparator.comparing(Post::getCreationDate, (p1, p2) -> p1.compareTo(p2));

        if (sort.equals("desc")) comparator = comparator.reversed();

        // 1-й ВАРИАНТ РЕШЕНИЯ

//        posts.sort(comparator);
//
//        if (from > posts.size()) {
//            return List.of();
//        } else {
//            if (size > posts.size()) {
//                return posts.subList(from, posts.size());
//            } else {
//                return posts.subList(from, posts.size()).subList(0, size);
//            }
//        }

        // 2-й ВАРИАНТ РЕШЕНИЯ
        return posts.stream()
                .sorted(comparator)
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Post create(Post post) {
        if (userService.findUserByEmail(post.getAuthor()) == null) {
            throw new UserNotFoundException("Пользователь " + post.getAuthor() + " не найден");
        }

        posts.add(post);
        return post;
    }

    public Post findById(Integer postId) {
        return posts.stream()
                .filter(post -> post.getId() == postId)
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException("Пост " + postId + " не найден"));
    }

    public List<Post> findAllPostsByFriends(String emailOfFriend, String sort, Integer size) {
        Comparator<Post> comparator = Comparator.comparing(Post::getCreationDate, (p1, p2) -> p1.compareTo(p2));

        if (sort.equals("desc")) comparator = comparator.reversed();

        return posts.stream()
                .filter(p -> emailOfFriend.equals(p.getAuthor()))
                .sorted(comparator)
                .limit(size)
                .collect(Collectors.toList());
    }
}