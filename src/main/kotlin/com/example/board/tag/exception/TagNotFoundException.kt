package com.example.board.tag.exception

class TagNotFoundException private constructor(
    message: String,
) : RuntimeException(message) {
    companion object {
        fun byId(tagId: Long) = TagNotFoundException("태그를 찾을 수 없습니다. id=$tagId")

        fun byName(tagName: String) = TagNotFoundException("태그를 찾을 수 없습니다. name=$tagName")
    }
}
