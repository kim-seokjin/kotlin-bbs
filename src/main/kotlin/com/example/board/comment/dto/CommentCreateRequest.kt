package com.example.board.comment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CommentCreateRequest(
    @field:NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    @field:Size(max = 1000)
    val content: String,
    @field:NotBlank(message = "작성자명은 비어있을 수 없습니다.")
    @field:Size(max = 50)
    val authorName: String,
)
