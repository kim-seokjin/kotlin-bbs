package com.example.board.tag.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PostTagRepository : JpaRepository<PostTag, Long> {
    fun findByPostId(postId: Long): List<PostTag>

    fun existsByPostIdAndTagId(
        postId: Long,
        tagId: Long,
    ): Boolean

    fun findByPostIdAndTagId(
        postId: Long,
        tagId: Long,
    ): PostTag?
}
