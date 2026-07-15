package com.example.board.like.dto

import com.example.board.like.domain.Like
import java.time.LocalDateTime

data class LikeResponse(
    val id: Long,
    val postId: Long,
    val likerName: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(like: Like): LikeResponse {
            return LikeResponse(
                id = requireNotNull(like.id),
                postId = like.postId,
                likerName = like.likerName,
                createdAt = like.createdAt,
            )
        }
    }
}
