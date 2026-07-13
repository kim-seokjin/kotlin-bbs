package com.example.board.comment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CommentUpdateRequest(
    @field:NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    @field:Size(max = 1000)
    val content: String,
    @field:NotBlank
    val requesterName: String,
)
