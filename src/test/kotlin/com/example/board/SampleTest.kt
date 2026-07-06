package com.example.board

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class SampleTest : BehaviorSpec({
    given("숫자 1과 2가 있을 때") {
        `when`("두 숫자를 더하면") {
            then("3이 나온다") {
                (1 + 2) shouldBe 3
            }
        }
    }
})
