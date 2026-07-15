package com.example.board.like.domain

import com.example.board.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "likes")
class Like protected constructor(
    postId: Long,
    likerName: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "post_id", nullable = false)
    var postId: Long = postId
        protected set

    @Column(name = "liker_name", nullable = false)
    var likerName: String = likerName
        protected set

    companion object {
        fun create(
            postId: Long,
            likerName: String,
        ): Like {
            return Like(postId, validateLikerName(likerName))
        }

        private fun validateLikerName(likerName: String): String {
            require(likerName.isNotBlank()) { "좋아요를 누른 사용자명은 비어 있을 수 없습니다." }
            require(likerName.length <= 30) { "사용자명은 30자 이하여야 합니다." }
            return likerName
        }
    }
}
