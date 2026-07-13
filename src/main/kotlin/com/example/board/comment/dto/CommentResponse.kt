package com.example.board.comment.dto

import com.example.board.comment.domain.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val postId: Long,
    val content: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(comment: Comment): CommentResponse {
            return CommentResponse(
                id = requireNotNull(comment.id),
                postId = comment.postId,
                content = comment.content,
                authorName = comment.authorName,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
            )
        }
    }
}
