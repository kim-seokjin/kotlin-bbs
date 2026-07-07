package com.example.board.post.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PostUpdateRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val title: String,
    @field:NotBlank
    @field:Size(max = 5000)
    val content: String,
    @field:NotBlank
    val requesterName: String,
)
