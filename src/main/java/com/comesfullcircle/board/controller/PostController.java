package com.comesfullcircle.board.controller;

import com.comesfullcircle.board.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

    //전체 게시물 Read
    @GetMapping("/api/v1/posts")
    public ResponseEntity<List<Post>> getPosts(){
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L,"Post 1", ZonedDateTime.now()));
        posts.add(new Post(2L,"Post 2", ZonedDateTime.now()));
        posts.add(new Post(3L,"Post 3", ZonedDateTime.now()));

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //postId 에 따른 게시물 Read
    @GetMapping("/api/v1/posts/{postId}")
    public ResponseEntity<Post> getPost(
            @PathVariable Long postId
    ){
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L,"Post 1", ZonedDateTime.now()));
        posts.add(new Post(2L,"Post 2", ZonedDateTime.now()));
        posts.add(new Post(3L,"Post 3", ZonedDateTime.now()));

        Optional<Post> matchingPost =
                posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        return matchingPost
                .map(post->ResponseEntity.ok().body(post))
                .orElse(ResponseEntity.notFound().build());
    }
}
