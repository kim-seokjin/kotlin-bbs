package com.example.board.like.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LikeCreateRequest(
    @field:NotBlank(message = "좋아요를 누른 사용자명은 비어있을 수 없습니다.")
    @field:Size(max = 30)
    val likerName: String,
)
