package com.nbe2.domain.posts.service.component;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.nbe2.domain.global.util.LockingUtil;
import com.nbe2.domain.posts.entity.Comment;
import com.nbe2.domain.posts.entity.Post;
import com.nbe2.domain.posts.repository.CommentRepository;

@Component
@RequiredArgsConstructor
public class CommentAppender {
    private final CommentRepository commentRepository;
    private final LockingUtil lockingUtil;

    public Long append(Comment comment) {
        Post post = comment.getPost();
        lockingUtil.pessimisticWriteLock(post);
        post.addComment();
        Comment saveComment = commentRepository.save(comment);
        return saveComment.getId();
    }
}
