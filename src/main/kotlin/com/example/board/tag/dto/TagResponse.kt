package com.example.board.tag.dto

import com.example.board.tag.domain.Tag

data class TagResponse(
    val id: Long,
    val name: String,
) {
    companion object {
        fun from(tag: Tag): TagResponse {
            return TagResponse(
                id = requireNotNull(tag.id),
                name = tag.name,
            )
        }
    }
}
