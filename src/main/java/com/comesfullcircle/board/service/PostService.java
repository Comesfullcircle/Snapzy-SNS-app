package com.comesfullcircle.board.service;

import com.comesfullcircle.board.model.Post;
import com.comesfullcircle.board.model.PostPatchRequestBody;
import com.comesfullcircle.board.model.PostPostRequestBody;
import com.comesfullcircle.board.model.entity.PostEntity;
import com.comesfullcircle.board.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;

    public List<Post> getPosts() {
        var postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId) {
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return Post.from(postEntity);
    }

    public Post createPost(PostPostRequestBody postPostRequestBody) {
        var postEntity = new PostEntity();
        postEntity.setBody(postPostRequestBody.body());
        var savedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(savedPostEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {
        Optional<Post> postOptional = posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if (postOptional.isPresent()) {
            Post postToUpdate = postOptional.get();
            postToUpdate.setBody(postPatchRequestBody.body());
            return postToUpdate;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }

    public void deletePost(Long postId) {
        Optional<Post> postOptional = posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if (postOptional.isPresent()) {
            posts.remove(postOptional.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }
}
