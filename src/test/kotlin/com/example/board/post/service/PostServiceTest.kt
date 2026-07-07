package com.example.board.post.service

import com.example.board.post.dto.PostCreateRequest
import com.example.board.post.dto.PostUpdateRequest
import com.example.board.post.exception.PostNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe


@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest(
    @Autowired private val postService: PostService,
) : BehaviorSpec({
    extensions(SpringExtension)
    
    given("유효한 요청이 주어졌을 때") {
        val request = PostCreateRequest("제목", "내용", "작성자")
        `when`("게시글을 작성하면") {
            val post = postService.create(request)
            then("id가 발급된다") {
                post.id shouldBe post.id
                post.id!! shouldBe post.id
            }
        }
    }
    
    given("존재하지 않는 게시글 id로") {
        `when`("수정을 요청하면") {
            then("PostNotFoundException이 발생한다") {
                shouldThrow<PostNotFoundException> {
                    postService.update(999_999L, PostUpdateRequest("제목", "내용", "작성자"))
                }
            }
        }
    }
})