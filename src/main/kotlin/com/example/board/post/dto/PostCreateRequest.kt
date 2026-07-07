package com.example.board.post.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PostCreateRequest(
    @field:NotBlank(message = "제목은 비어있을 수 없습니다.")
    @field:Size(max = 100)
    val title: String,
    @field:NotBlank(message = "내용은 비어있을 수 없습니다.")
    @field:Size(max = 5000)
    val content: String,
    @field:NotBlank(message = "작성자명은 비어있을 수 없습니다.")
    @field:Size(max = 30)
    val authorName: String,
)
