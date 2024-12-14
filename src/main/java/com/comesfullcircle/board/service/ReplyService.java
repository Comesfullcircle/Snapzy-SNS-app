package com.comesfullcircle.board.service;

import com.comesfullcircle.board.exception.post.PostNotFoundException;
import com.comesfullcircle.board.exception.reply.ReplyNotFoundException;
import com.comesfullcircle.board.exception.user.UserNotAllowedException;
import com.comesfullcircle.board.exception.user.UserNotFoundException;
import com.comesfullcircle.board.model.entity.PostEntity;
import com.comesfullcircle.board.model.entity.ReplyEntity;
import com.comesfullcircle.board.model.entity.UserEntity;
import com.comesfullcircle.board.model.post.Post;
import com.comesfullcircle.board.model.post.PostPatchRequestBody;
import com.comesfullcircle.board.model.post.PostPostRequestBody;
import com.comesfullcircle.board.model.reply.Reply;
import com.comesfullcircle.board.model.reply.ReplyPatchRequestBody;
import com.comesfullcircle.board.model.reply.ReplyPostRequestBody;
import com.comesfullcircle.board.repository.PostEntityRepository;
import com.comesfullcircle.board.repository.ReplyEntityRepository;
import com.comesfullcircle.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Service
public class ReplyService {

    @Autowired private PostEntityRepository postEntityRepository;
    @Autowired private UserEntityRepository userEntityRepository;

    @Autowired private ReplyEntityRepository replyEntityRepository;

    public List<Reply> getRepliesByPostId(Long postId) {
        var postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntities = replyEntityRepository.findByPost(postEntity);
        return replyEntities.stream().map(Reply::from).toList();
    }

    public List<Reply> getRepliesByUser(String username) {
        var user =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        var replyEntities = replyEntityRepository.findByUser(user);

        return replyEntities.stream().map(Reply::from).toList();
    }

    @Transactional
    public Reply createReply(
            Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {
        var postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        ReplyEntity replyEntity =
                replyEntityRepository.save(
                        ReplyEntity.of(replyPostRequestBody.body(), currentUser, postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

        return Reply.from(replyEntity);
    }

    public Reply updateReply(
            Long postId,
            Long replyId,
            ReplyPatchRequestBody replyPatchRequestBody,
            UserEntity currentUser) {
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntity =
                replyEntityRepository
                        .findById(replyId)
                        .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(replyPatchRequestBody.body());
        return Reply.from(replyEntityRepository.save(replyEntity));
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        PostEntity postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntity =
                replyEntityRepository
                        .findById(replyId)
                        .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);
        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }
}