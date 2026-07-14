package com.example.board.tag.domain

import com.example.board.post.dto.PostSummaryResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TagPostQueryRepository {
    fun findPostsByTagName(
        tagName: String,
        pageable: Pageable,
    ): Page<PostSummaryResponse>
}
