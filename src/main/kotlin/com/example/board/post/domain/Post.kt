package com.example.board.post.domain

import com.example.board.common.BaseEntity
import com.example.board.common.exception.ForbiddenException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class Post protected constructor(
    title: String,
    content: String,
    authorName: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false, length = 100)
    var title: String = title
        protected set

    @Column(nullable = false, length = 5000)
    var content: String = content
        protected set

    @Column(nullable = false, length = 30)
    var authorName: String = authorName
        protected set

    @Column
    var deletedAt: LocalDateTime? = null
        protected set

    val isDeleted: Boolean
        get() = deletedAt != null

    fun update(
        title: String,
        content: String,
        requesterName: String,
    ) {
        validateOwner(requesterName)
        this.title = validateTitle(title)
        this.content = validateContent(content)
    }

    fun delete(requesterName: String) {
        validateOwner(requesterName)
        if (isDeleted) {
            throw ForbiddenException("이미 삭제된 게시글입니다.")
        }
        this.deletedAt = LocalDateTime.now()
    }

    fun validateOwner(requesterName: String) {
        if (authorName != requesterName) {
            throw ForbiddenException("게시글 수정 권한이 없습니다.")
        }
    }

    companion object {
        fun create(
            title: String,
            content: String,
            authorname: String,
        ): Post {
            return Post(
                title = validateTitle(title),
                content = validateContent(content),
                authorName = validateAuthorName(authorname),
            )
        }

        private fun validateTitle(title: String): String {
            require(title.isNotBlank()) { "제목은 비어있을 수 없습니다." }
            require(title.length <= 100) { "제목은 100자 이하여야 합니다." }
            return title
        }

        private fun validateContent(content: String): String {
            require(content.isNotBlank()) { "내용은 비어있을 수 없습니다." }
            require(content.length <= 5000) { "내용은 5000자 이하여야 합니다." }
            return content
        }

        private fun validateAuthorName(authorname: String): String {
            require(authorname.isNotBlank()) { "작성자명은 비어있을 수 없습니다." }
            require(authorname.length <= 30) { "작성자명은 30자 이하여야 합니다." }
            return authorname
        }
    }
}
