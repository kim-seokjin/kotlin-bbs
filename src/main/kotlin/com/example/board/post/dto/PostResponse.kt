package com.example.board.post.dto

import com.example.board.post.domain.Post
import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(post: Post): PostResponse {
            return PostResponse(
                id = requireNotNull(post.id),
                title = post.title,
                content = post.content,
                authorName = post.authorName,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
            )
        }
    }
}
