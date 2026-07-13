package com.example.board.comment.domain

import com.example.board.common.exception.ForbiddenException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CommentTest : BehaviorSpec({

    given("유효한 값으로 Comment.create를 호출하면") {
        val comment = Comment.create(1L, "내용", "작성자")

        then("필드가 그대로 설정된다") {
            comment.postId shouldBe 1L
            comment.content shouldBe "내용"
            comment.authorName shouldBe "작성자"
            comment.isDeleted shouldBe false
            comment.deletedAt shouldBe null
        }
    }

    given("create시 내용이") {
        then("비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Comment.create(1L, "", "작성자")
            }.message shouldBe "댓글 내용은 비어있을 수 없습니다."
        }

        then("1000자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Comment.create(1L, "a".repeat(1001), "작성자")
            }.message shouldBe "댓글 내용은 1000자 이하여야 합니다."
        }
    }

    given("create 시 작성자명이") {
        then("비어 있으면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Comment.create(1L, "내용", "")
            }.message shouldBe "작성자명은 비어있을 수 없습니다."
        }

        then("30자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                Comment.create(1L, "내용", "a".repeat(31))
            }.message shouldBe "작성자명은 30자 이하여야 합니다."
        }
    }

    given("작성자가 본인 댓글을 update하면") {
        val comment = Comment.create(1L, "내용", "작성자")

        comment.update("새 내용", "작성자")

        then("내용이 갱신된다") {
            comment.content shouldBe "새 내용"
        }
    }

    given("update 시") {
        val comment = Comment.create(1L, "내용", "작성자")

        then("요청자가 작성자가 아니면 ForbiddenException이 발생한다") {
            shouldThrow<ForbiddenException> {
                comment.update("새 내용", "다른사람")
            }.message shouldBe "댓글 수정 권한이 없습니다."
        }

        then("내용이 1000자를 초과하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                comment.update("a".repeat(1001), "작성자")
            }.message shouldBe "댓글 내용은 1000자 이하여야 합니다."
        }
    }

    given("작성자가 본인 댓글을 delete하면") {
        val comment = Comment.create(1L, "내용", "작성자")

        comment.delete("작성자")

        then("soft delete 된다") {
            comment.isDeleted shouldBe true
            comment.deletedAt shouldNotBe null
        }
    }

    given("delete 시") {
        then("요청자가 작성자가 아니면 ForbiddenException이 발생한다") {
            val comment = Comment.create(1L, "내용", "작성자")

            shouldThrow<ForbiddenException> {
                comment.delete("다른사람")
            }.message shouldBe "댓글 수정 권한이 없습니다."
        }

        then("이미 삭제된 댓글이면 ForbiddenException이 발생한다") {
            val comment = Comment.create(1L, "내용", "작성자")
            comment.delete("작성자")

            shouldThrow<ForbiddenException> {
                comment.delete("작성자")
            }.message shouldBe "이미 삭제된 댓글입니다."
        }
    }
})
