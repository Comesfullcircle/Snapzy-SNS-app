package com.comesfullcircle.board.controller;

import com.comesfullcircle.board.model.entity.ReplyEntity;
import com.comesfullcircle.board.model.entity.UserEntity;
import com.comesfullcircle.board.model.post.Post;
import com.comesfullcircle.board.model.post.PostPatchRequestBody;
import com.comesfullcircle.board.model.post.PostPostRequestBody;
import com.comesfullcircle.board.model.reply.Reply;
import com.comesfullcircle.board.model.reply.ReplyPatchRequestBody;
import com.comesfullcircle.board.model.reply.ReplyPostRequestBody;
import com.comesfullcircle.board.service.PostService;
import com.comesfullcircle.board.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}")
public class ReplyController {
    @Autowired private ReplyService replyService;

    @GetMapping("/replies")
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {
        var replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping("/replies")
    public ResponseEntity<Reply> createReply(
            @PathVariable Long postId,
            @RequestBody ReplyPostRequestBody replyPostRequestBody,
            Authentication authentication) {
        var reply =
                replyService.createReply(
                        postId, replyPostRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @PatchMapping("/replies/{replyId}")
    public ResponseEntity<Reply> updatePost(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
            Authentication authentication) {
        var reply =
                replyService.updateReply(
                        postId, replyId, replyPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long postId, @PathVariable Long replyId, Authentication authentication) {
        replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }
}