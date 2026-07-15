package com.example.board.like.service

import com.example.board.like.domain.LikeRepository
import com.example.board.like.exception.DuplicateLikeException
import com.example.board.like.exception.LikeNotFoundException
import com.example.board.post.dto.PostCreateRequest
import com.example.board.post.exception.PostNotFoundException
import com.example.board.post.service.PostService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class LikeServiceTest(
    @Autowired private val likeService: LikeService,
    @Autowired private val likeRepository: LikeRepository,
    @Autowired private val postService: PostService,
) : BehaviorSpec({
        extensions(SpringExtension)

        afterEach {
            likeRepository.deleteAll()
        }

        given("존재하는 게시글에") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))

            `when`("좋아요를 누르면") {
                val like = likeService.like(post.id!!, "팬")

                then("좋아요가 생성된다") {
                    like.postId shouldBe post.id
                    like.likerName shouldBe "팬"
                }
            }
        }

        given("이미 좋아요를 누른 사용자가") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            likeService.like(post.id!!, "팬")

            `when`("같은 게시글에 다시 좋아요를 누르면") {
                then("DulicateLikeException이 발생한다") {
                    shouldThrow<DuplicateLikeException> {
                        likeService.like(post.id!!, "팬")
                    }
                }
            }
        }

        given("존재하지 않는 게시글에") {
            `when`("좋아요를 누르면") {
                then("PostNotFoundException이 발생한다") {
                    shouldThrow<PostNotFoundException> {
                        likeService.like(999_999L, "팬")
                    }
                }
            }
        }

        given("좋아요를 누른 사용자가") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            likeService.like(post.id!!, "팬")

            `when`("좋아요를 취소하면") {
                likeService.unlike(post.id!!, "팬")

                then("좋아요 개수가 0이 된다") {
                    likeRepository.findAll().size shouldBe 0
                }
            }
        }

        given("좋아요를 누르지 않은 사용자가") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))

            `when`("좋아요 취소를 요청하면") {
                then("LikeNotFoundException이 발생한다") {
                    shouldThrow<LikeNotFoundException> {
                        likeService.unlike(post.id!!, "팬")
                    }
                }
            }
        }

        given("좋아요가 여러 개 눌렸을 때") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            likeService.like(post.id!!, "팬1")
            likeService.like(post.id!!, "팬2")
            likeService.like(post.id!!, "팬3")

            `when`("좋아요 개수를 조회하면") {
                val count = likeService.countLikes(post.id!!)
                then("3이 반환된다") {
                    count shouldBe 3
                }
            }
        }

        given("좋아요를 누른 적 없는 게시글의") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))

            `when`("좋아요 개수를 조회하면") {
                then("0이 반환된다") {
                    val count = likeService.countLikes(post.id!!)
                    count shouldBe 0
                }
            }
        }

        given("사용자가 좋아요를 누른 상태에서") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            likeService.like(post.id!!, "팬")

            `when`("본인의 좋아요 여부를 조회하면") {
                then("true가 반환된다") {
                    likeService.isLiked(post.id!!, "팬") shouldBe true
                }
            }
        }

        given("사용자가 좋아요르 누르지 않은 상태에서") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))

            `when`("좋아요 여부를 조회하면") {
                then("false가 반환된다") {
                    likeService.isLiked(post.id!!, "팬") shouldBe false
                }
            }
        }
    })
