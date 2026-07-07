package com.example.board.post.domain

import com.example.board.post.dto.PostSummaryResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostQueryRepository {
    fun findPostSummaries(pageable: Pageable): Page<PostSummaryResponse>
}
