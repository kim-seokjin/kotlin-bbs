package com.example.board.tag.domain

import com.example.board.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "post_tags")
class PostTag protected constructor(
    postId: Long,
    tagId: Long,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "post_id", nullable = false)
    var postId: Long = postId
        protected set

    @Column(name = "tag_id", nullable = false)
    var tagId: Long = tagId
        protected set

    companion object {
        fun create(
            postId: Long,
            tagId: Long,
        ): PostTag {
            return PostTag(postId, tagId)
        }
    }
}
