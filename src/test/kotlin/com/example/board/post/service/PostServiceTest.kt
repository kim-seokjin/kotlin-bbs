package com.example.board.post.service

import com.example.board.post.domain.PostRepository
import com.example.board.post.dto.PostCreateRequest
import com.example.board.post.dto.PostUpdateRequest
import com.example.board.post.exception.PostNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest(
    @Autowired private val postService: PostService,
    @Autowired private val postRepository: PostRepository,
) : BehaviorSpec({
        extensions(SpringExtension)

        beforeEach {
            postRepository.deleteAll()
        }

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

        given("존재하는 게시글 id로") {
            val post = postService.create(PostCreateRequest("제목", "내용", "작성자"))
            `when`("단건 조회하면") {
                val found = postService.getPost(post.id!!)
                then("게시글 정보를 반환한다") {
                    found.title shouldBe "제목"
                }
            }
        }

        given("삭제된 게시글 id로") {
            val post = postService.create(PostCreateRequest("제목", "내용", "작성자"))
            postService.delete(post.id!!, "작성자")
            `when`("단건 조회하면") {
                then("PostNotFoundException이 발생한다") {
                    shouldThrow<PostNotFoundException> {
                        postService.getPost(post.id!!)
                    }
                }
            }
        }

        given("게시글이 여러 개 존재할 때") {
            postService.create(PostCreateRequest("첫번째", "내용1", "작성자"))
            postService.create(PostCreateRequest("두번째", "내용2", "작성자"))
            postService.create(PostCreateRequest("세번째", "내용3", "작성자"))

            `when`("목록을 조회하면") {
                val page = postRepository.findPostSummaries(PageRequest.of(0, 2))
                then("페이징된 최신순 목록을 반환한다") {
                    page.content.size shouldBe 2
                    page.content.first().title shouldBe "세번째"
                    page.totalElements shouldBe 3
                }
            }
        }
    })
