package com.example.board.post.dto

import java.time.LocalDateTime

data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val authorName: String,
    val createdAt: LocalDateTime,
)
