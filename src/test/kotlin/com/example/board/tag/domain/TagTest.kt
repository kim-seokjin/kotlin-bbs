package com.example.board.tag.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TagTest : BehaviorSpec({

    given("유효한 이름으로 Tag.create를 호출하면") {
        val tag = Tag.create("kotlin")

        then("이름이 그대로 설정된다") {
            tag.name shouldBe "kotlin"
        }
    }

    given("이름 앞뒤에 공백이 있으면") {
        val tag = Tag.create(" kotlin ")

        then("trim되어 저장된다") {
            tag.name shouldBe "kotlin"
        }
    }

    given("create 시 이름이") {
        then("비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Tag.create("   ")
            }.message shouldBe "태그명은 비어있을 수 없습니다."
        }

        then("20자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Tag.create("a".repeat(21))
            }.message shouldBe "태그명은 20자 이하여야 합니다."
        }
    }
})
