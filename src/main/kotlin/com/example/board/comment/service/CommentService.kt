package com.example.board.comment.service

import com.example.board.comment.domain.Comment
import com.example.board.comment.domain.CommentRepository
import com.example.board.comment.dto.CommentCreateRequest
import com.example.board.comment.dto.CommentUpdateRequest
import com.example.board.comment.exception.CommentNotFoundException
import com.example.board.post.domain.Post
import com.example.board.post.domain.PostRepository
import com.example.board.post.exception.PostNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun create(
        postId: Long,
        request: CommentCreateRequest,
    ): Comment {
        val post = findActivePost(postId)
        val comment = Comment.create(post.id!!, request.content, request.authorName)
        return commentRepository.save(comment)
    }

    @Transactional
    fun update(
        commentId: Long,
        request: CommentUpdateRequest,
    ): Comment {
        val comment = findActiveComment(commentId)
        comment.update(request.content, request.requesterName)
        return comment
    }

    @Transactional
    fun delete(
        commentId: Long,
        requesterName: String,
    ) {
        val comment = findActiveComment(commentId)
        comment.delete(requesterName)
    }

    @Transactional(readOnly = true)
    fun getComments(postId: Long): List<Comment> {
        findActivePost(postId)
        return commentRepository.findByPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(postId)
    }

    private fun findActivePost(postId: Long): Post {
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException(postId) }
        if (post.isDeleted) {
            throw PostNotFoundException(postId)
        }
        return post
    }

    private fun findActiveComment(commentId: Long): Comment {
        val comment = commentRepository.findById(commentId).orElseThrow { CommentNotFoundException(commentId) }
        if (comment.isDeleted) {
            throw CommentNotFoundException(commentId)
        }
        return comment
    }
}
