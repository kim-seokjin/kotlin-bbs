package com.example.board.like.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class LikeTest : BehaviorSpec({

    given("유효한 값으로 Like.create를 호출하면") {
        val like = Like.create(postId = 1L, likerName = "test")

        then("필드가 그대로 설정된다") {
            like.postId shouldBe 1L
            like.likerName shouldBe "test"
        }
    }

    given("create시 사용자명이") {
        then("비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Like.create(1L, "")
            }.message shouldBe "좋아요를 누른 사용자명은 비어 있을 수 없습니다."
        }

        then("30자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Like.create(1L, "a".repeat(31))
            }.message shouldBe "사용자명은 30자 이하여야 합니다."
        }
    }
})
