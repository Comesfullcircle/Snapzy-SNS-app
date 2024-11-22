package com.comesfullcircle.board.controller;

import com.comesfullcircle.board.model.entity.UserEntity;
import com.comesfullcircle.board.model.post.Post;
import com.comesfullcircle.board.model.post.PostPatchRequestBody;
import com.comesfullcircle.board.model.post.PostPostRequestBody;
import com.comesfullcircle.board.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    public static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    //전체 게시물 Read
    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
       // System.out.println("GET /api/v1/posts");
        logger.info("GET /api/v1/posts");
        var posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    //postId 에 따른 게시물 Read
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(
            @PathVariable Long postId
    ){
        logger.info("GET /api/v1/posts/{}", postId);
        var post  = postService.getPostByPostId(postId);
        return ResponseEntity.ok(post);
    }

    // 게시물 작성 Post /posts
    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody  PostPostRequestBody postPostRequestBody, Authentication authentication)
    {
        logger.info("POST /api/v1/posts");
        var post = postService.createPost(postPostRequestBody,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    //update 수정하기
    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody postPatchRequestBody,
            Authentication authentication
    ){
        logger.info("PATCH /api/v1/posts/{}", postId);
        var post = postService.updatePost(postId, postPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }


    //delete 삭제하기
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication)
    {
        logger.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
