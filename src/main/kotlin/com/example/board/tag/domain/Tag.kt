package com.example.board.tag.domain

import com.example.board.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tags")
class Tag protected constructor(
    name: String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false, length = 20, unique = true)
    var name: String = name
        protected set

    companion object {
        fun create(name: String): Tag {
            return Tag(validateName(name))
        }

        private fun validateName(name: String): String {
            val trimmed = name.trim()
            require(trimmed.isNotBlank()) { "태그명은 비어있을 수 없습니다." }
            require(trimmed.length <= 20) { "태그명은 20자 이하여야 합니다." }
            return trimmed
        }
    }
}
