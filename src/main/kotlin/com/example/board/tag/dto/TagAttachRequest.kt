package com.example.board.tag.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class TagAttachRequest(
    @field:NotEmpty(message = "태그명 목록은 비어있을 수 없습니다.")
    val tagNames: List<@NotBlank String>,
    @field:NotBlank(message = "요청자명은 비어있을 수 없습니다.")
    val requesterName: String,
)
