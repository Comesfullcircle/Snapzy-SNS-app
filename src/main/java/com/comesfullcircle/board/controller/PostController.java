package com.comesfullcircle.board.controller;

import com.comesfullcircle.board.model.Post;
import com.comesfullcircle.board.model.PostPatchRequestBody;
import com.comesfullcircle.board.model.PostPostRequestBody;
import com.comesfullcircle.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    //전체 게시물 Read
    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        var posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    //postId 에 따른 게시물 Read
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(
            @PathVariable Long postId
    ){
        Optional<Post> matchingPost = postService.getPostByPostId(postId);
        return matchingPost.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 게시물 작성 Post /posts
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody  PostPostRequestBody postPostRequestBody)
    {
       var post = postService.createPost(postPostRequestBody);
       return ResponseEntity.ok(post);
    }


    //update 수정하기
    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody postPatchRequestBody
    ){
        var post = postService.updatePost(postId, postPatchRequestBody);
        return ResponseEntity.ok(post);
    }
}
