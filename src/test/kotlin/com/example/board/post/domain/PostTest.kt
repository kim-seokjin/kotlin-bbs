package com.example.board.post.domain

import com.example.board.common.exception.ForbiddenException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PostTest : BehaviorSpec({

    given("유효한 값으로 Post.create를 호출하면") {
        val post = Post.create("제목", "내용", "작성자")

        then("필드가 그대로 설정된다") {
            post.title shouldBe "제목"
            post.content shouldBe "내용"
            post.authorName shouldBe "작성자"
            post.isDeleted shouldBe false
            post.deletedAt shouldBe null
        }
    }

    given("create 시 제목이") {
        then("비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Post.create("", "내용", "작성자")
            }.message shouldBe "제목은 비어있을 수 없습니다."
        }

        then("100자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Post.create("a".repeat(101), "내용", "작성자")
            }.message shouldBe "제목은 100자 이하여야 합니다."
        }
    }

    given("create 시 내용이") {
        then("비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Post.create("제목", "", "작성자")
            }.message shouldBe "내용은 비어있을 수 없습니다."
        }

        then("5000자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Post.create("제목", "a".repeat(5001), "작성자")
            }.message shouldBe "내용은 5000자 이하여야 합니다."
        }
    }

    given("create 시 작성자명이") {
        then("비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Post.create("제목", "내용", "")
            }.message shouldBe "작성자명은 비어있을 수 없습니다."
        }

        then("30자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Post.create("제목", "내용", "a".repeat(31))
            }.message shouldBe "작성자명은 30자 이하여야 합니다."
        }
    }

    given("작성자가 본인 게시글을 update하면") {
        val post = Post.create("제목", "내용", "작성자")

        post.update("새 제목", "새 내용", "작성자")

        then("제목과 내용이 갱신된다") {
            post.title shouldBe "새 제목"
            post.content shouldBe "새 내용"
        }
    }

    given("update 시") {
        val post = Post.create("제목", "내용", "작성자")

        then("요청자가 작성자가 아니면 ForbiddenException이 발생한다") {
            shouldThrow<ForbiddenException> {
                post.update("새 제목", "새 내용", "다른사람")
            }.message shouldBe "게시글 수정 권한이 없습니다."
        }

        then("제목이 비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                post.update("", "내용", "작성자")
            }.message shouldBe "제목은 비어있을 수 없습니다."
        }

        then("내용이 5000자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                post.update("제목", "a".repeat(5001), "작성자")
            }.message shouldBe "내용은 5000자 이하여야 합니다."
        }
    }

    given("작성자가 본인 게시글을 delete하면") {
        val post = Post.create("제목", "내용", "작성자")

        post.delete("작성자")

        then("soft delete 된다") {
            post.isDeleted shouldBe true
            post.deletedAt shouldNotBe null
        }
    }

    given("delete 시") {
        then("요청자가 작성자가 아니면 ForbiddenException이 발생한다") {
            val post = Post.create("제목", "내용", "작성자")

            shouldThrow<ForbiddenException> {
                post.delete("다른사람")
            }.message shouldBe "게시글 수정 권한이 없습니다."
        }

        then("이미 삭제된 게시글이면 ForbiddenException이 발생한다") {
            val post = Post.create("제목", "내용", "작성자")
            post.delete("작성자")

            shouldThrow<ForbiddenException> {
                post.delete("작성자")
            }.message shouldBe "이미 삭제된 게시글입니다."
        }
    }

    given("validateOwner를 호출할 때") {
        val post = Post.create("제목", "내용", "작성자")

        then("작성자명과 일치하면 통과한다") {
            post.validateOwner("작성자")
        }

        then("작성자명과 다르면 ForbiddenException이 발생한다") {
            shouldThrow<ForbiddenException> {
                post.validateOwner("다른사람")
            }.message shouldBe "게시글 수정 권한이 없습니다."
        }
    }
})
