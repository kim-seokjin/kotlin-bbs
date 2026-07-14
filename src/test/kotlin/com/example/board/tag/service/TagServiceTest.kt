package com.example.board.tag.service

import com.example.board.common.exception.ForbiddenException
import com.example.board.post.dto.PostCreateRequest
import com.example.board.post.exception.PostNotFoundException
import com.example.board.post.service.PostService
import com.example.board.tag.domain.PostTagRepository
import com.example.board.tag.domain.TagRepository
import com.example.board.tag.dto.TagAttachRequest
import com.example.board.tag.exception.TagNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class TagServiceTest(
    @Autowired private val tagService: TagService,
    @Autowired private val tagRepository: TagRepository,
    @Autowired private val postTagRepository: PostTagRepository,
    @Autowired private val postService: PostService,
) : BehaviorSpec({
        extensions(SpringExtension)

        afterEach {
            postTagRepository.deleteAll()
            tagRepository.deleteAll()
        }

        given("존재하는 게시글에 새로운 태그명으로") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))

            `when`("태그를 추가하면") {
                val tags = tagService.attachTags(post.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))

                then("태그가 새로 생성되어 연결된다") {
                    tags.size shouldBe 1
                    tags[0].name shouldBe "kotlin"
                }
            }
        }

        given("이미 존재하는 태그명으로 다른 게시글에 태그를 추가하면") {
            val postA = postService.create(PostCreateRequest("A", "내용", "글쓴이"))
            val postB = postService.create(PostCreateRequest("B", "내용", "글쓴이"))
            val firstTag = tagService.attachTags(postA.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))[0]

            `when`("동일한 태그명으로 요청하면") {
                val secondTag = tagService.attachTags(postB.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))[0]

                then("태그가 재사용된다 (동일한 id)") {
                    secondTag.id shouldBe firstTag.id
                }
            }
        }

        given("이미 첨부된 태그를 같은 게시글에 다시 추가해도") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            tagService.attachTags(post.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))

            `when`("동일한 태그명으로 다시 요청하면") {
                tagService.attachTags(post.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))

                then("중복 연결되지 않는다") {
                    postTagRepository.findByPostId(post.id!!).size shouldBe 1
                    tagService.getTagsOfPost(post.id!!).size shouldBe 1
                }
            }
        }

        given("작성자가 아닌 사용자가") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))

            `when`("태그 추가를 요청하면") {
                then("ForbiddenException이 발생한다") {
                    shouldThrow<ForbiddenException> {
                        tagService.attachTags(post.id!!, TagAttachRequest(listOf("kotlin"), "다른사람"))
                    }
                }
            }
        }

        given("존재하지 않는 게시글에") {
            `when`("태그 추가를 요청하면") {
                then("PostNotFoundException이 발생한다") {
                    shouldThrow<PostNotFoundException> {
                        tagService.attachTags(999_999L, TagAttachRequest(listOf("kotlin"), "글쓴이"))
                    }
                }
            }
        }

        given("게시글에 연결된 태그를") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            val tag = tagService.attachTags(post.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))[0]

            `when`("작성자가 제거하면") {
                tagService.detachTag(post.id!!, tag.id!!, "글쓴이")

                then("게시글의 태그 목록에서 사라진다") {
                    tagService.getTagsOfPost(post.id!!).size shouldBe 0
                }
            }
        }

        given("태그 제거 시") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            val tag = tagService.attachTags(post.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))[0]

            then("작성자가 아니면 ForbiddenException이 발생한다") {
                shouldThrow<ForbiddenException> {
                    tagService.detachTag(post.id!!, tag.id!!, "다른사람")
                }
            }

            then("연결되지 않은 태그 id면 TagNotFoundException이 발생한다") {
                shouldThrow<TagNotFoundException> {
                    tagService.detachTag(post.id!!, 999_999L, "글쓴이")
                }
            }
        }

        given("태그명이 여러 개 있을 때") {
            val post = postService.create(PostCreateRequest("제목", "내용", "글쓴이"))
            tagService.attachTags(post.id!!, TagAttachRequest(listOf("kotlin", "kotest", "spring"), "글쓴이"))

            `when`("'kot'으로 검색하면") {
                val result = tagService.searchTags("kot")

                then("kotlin, kotest만 이름순으로 반환된다") {
                    result.size shouldBe 2
                    result.map { it.name } shouldBe listOf("kotest", "kotlin")
                }
            }
        }

        given("태그가 달린 게시글이 여러 개 있을 때") {
            val postA = postService.create(PostCreateRequest("A", "내용", "글쓴이"))
            val postB = postService.create(PostCreateRequest("B", "내용", "글쓴이"))
            val postC = postService.create(PostCreateRequest("C", "내용", "글쓴이"))
            tagService.attachTags(postA.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))
            tagService.attachTags(postB.id!!, TagAttachRequest(listOf("kotlin"), "글쓴이"))
            tagService.attachTags(postC.id!!, TagAttachRequest(listOf("java"), "글쓴이"))

            `when`("'kotlin' 태그로 게시글을 조회하면") {
                val page = tagService.getPostsByTag("kotlin", PageRequest.of(0, 10))

                then("kotlin 태그가 달린 게시글만 반환된다") {
                    page.totalElements shouldBe 2
                    page.content.map { it.title } shouldContainExactlyInAnyOrder listOf("A", "B")
                }
            }
        }

        given("존재하지 않는 태그명으로") {
            `when`("게시글을 조회하면") {
                then("TagNotFoundException이 발생한다") {
                    shouldThrow<TagNotFoundException> {
                        tagService.getPostsByTag("없는태그", PageRequest.of(0, 10))
                    }
                }
            }
        }
    })
