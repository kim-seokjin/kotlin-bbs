package com.example.board.like.domain

import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long> {
    fun existsByPostIdAndLikerName(
        postId: Long,
        likerName: String,
    ): Boolean

    fun findByPostIdAndLikerName(
        postId: Long,
        likerName: String,
    ): Like?

    fun countByPostId(postId: Long): Long
}
