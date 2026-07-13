package com.example.board.comment.domain

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
@Table(name = "comments")
class Comment protected constructor(
    postId: Long,
    content: String,
    authorName: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "post_id", nullable = false)
    var postId: Long = postId
        protected set

    @Column(nullable = false, length = 1000)
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
        content: String,
        requesterName: String,
    ) {
        validateOwner(requesterName)
        this.content = validateContent(content)
    }

    fun delete(requesterName: String) {
        validateOwner(requesterName)
        if (isDeleted) {
            throw ForbiddenException("이미 삭제된 댓글입니다.")
        }
        this.deletedAt = LocalDateTime.now()
    }

    private fun validateOwner(requesterName: String) {
        if (this.authorName != requesterName) {
            throw ForbiddenException("댓글 수정 권한이 없습니다.")
        }
    }

    companion object {
        fun create(
            postId: Long,
            content: String,
            authorName: String,
        ): Comment {
            return Comment(
                postId = postId,
                content = validateContent(content),
                authorName = validateAuthorName(authorName),
            )
        }

        private fun validateContent(content: String): String {
            require(content.isNotBlank()) { "댓글 내용은 비어있을 수 없습니다." }
            require(content.length <= 1000) { "댓글 내용은 1000자 이하여야 합니다." }
            return content
        }

        private fun validateAuthorName(authorName: String): String {
            require(authorName.isNotBlank()) { "작성자명은 비어있을 수 없습니다." }
            require(authorName.length <= 30) { "작성자명은 30자 이하여야 합니다." }
            return authorName
        }
    }
}
