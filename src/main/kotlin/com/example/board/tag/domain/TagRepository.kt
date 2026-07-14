package com.example.board.tag.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(name: String): Tag?

    fun findTop10ByNameContainingIgnoreCaseOrderByNameAsc(keyword: String): List<Tag>
}
