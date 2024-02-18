package ru.yandex.practicum.catsgram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostFeedController {
    private final PostService postService;

    @Autowired
    public PostFeedController(PostService postService, UserService userService) {
        this.postService = postService;
    }

    @PostMapping(value = "/feed/friends")
    public List<Post> friendsPosts(@RequestBody(required = false) String data) {
        ObjectMapper mapper = new ObjectMapper();
        Params params;

        try {
            String stringParams = mapper.readValue(data, String.class);
            params = mapper.readValue(stringParams, Params.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Неверный формат JSON", e);
        }

        if (params != null) {
            List<Post> listOfFriendsPosts = new ArrayList<>();

            for (String friend : params.getFriends()) {
                listOfFriendsPosts.addAll(postService.findAllPostsByFriends(friend, params.getSort(), params.getSize()));
            }

            return listOfFriendsPosts;
        } else {
            throw new RuntimeException("Неверно переданы / заполнены параметры");
        }
    }

    static class Params {
        private String sort;
        private Integer size;
        private List<String> friends;

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public List<String> getFriends() {
            return friends;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }
    }
}
