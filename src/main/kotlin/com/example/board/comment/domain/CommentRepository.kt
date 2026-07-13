package com.example.board.comment.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(postId: Long): List<Comment>
}
