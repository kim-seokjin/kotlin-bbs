package com.example.board.tag.service

import com.example.board.post.domain.Post
import com.example.board.post.domain.PostRepository
import com.example.board.post.dto.PostSummaryResponse
import com.example.board.post.exception.PostNotFoundException
import com.example.board.tag.domain.PostTag
import com.example.board.tag.domain.PostTagRepository
import com.example.board.tag.domain.Tag
import com.example.board.tag.domain.TagPostQueryRepository
import com.example.board.tag.domain.TagRepository
import com.example.board.tag.dto.TagAttachRequest
import com.example.board.tag.exception.TagNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val postTagRepository: PostTagRepository,
    private val postRepository: PostRepository,
    private val tagPostQueryRepository: TagPostQueryRepository,
) {
    @Transactional
    fun attachTags(
        postId: Long,
        request: TagAttachRequest,
    ): List<Tag> {
        val post = findActivePost(postId)
        post.validateOwner(request.requesterName)

        return request.tagNames.distinct().map { tagName ->
            val tag = tagRepository.findByName(tagName.trim()) ?: tagRepository.save(Tag.create(tagName))
            if (!postTagRepository.existsByPostIdAndTagId(post.id!!, tag.id!!)) {
                postTagRepository.save(PostTag.create(post.id!!, tag.id!!))
            }
            tag
        }
    }

    @Transactional
    fun detachTag(
        postId: Long,
        tagId: Long,
        requesterName: String,
    ) {
        val post = findActivePost(postId)
        post.validateOwner(requesterName)

        val postTag =
            postTagRepository.findByPostIdAndTagId(postId, tagId)
                ?: throw TagNotFoundException.byId(tagId)
        postTagRepository.delete(postTag)
    }

    @Transactional(readOnly = true)
    fun getTagsOfPost(postId: Long): List<Tag> {
        findActivePost(postId)
        val tagIds = postTagRepository.findByPostId(postId).map { it.tagId }
        if (tagIds.isEmpty()) return emptyList()
        return tagRepository.findAllById(tagIds)
    }

    @Transactional(readOnly = true)
    fun searchTags(keyword: String): List<Tag> {
        return tagRepository.findTop10ByNameContainingIgnoreCaseOrderByNameAsc(keyword)
    }

    @Transactional(readOnly = true)
    fun getPostsByTag(
        tagName: String,
        pageable: Pageable,
    ): Page<PostSummaryResponse> {
        tagRepository.findByName(tagName) ?: throw TagNotFoundException.byName(tagName)
        return tagPostQueryRepository.findPostsByTagName(tagName, pageable)
    }

    private fun findActivePost(postId: Long): Post {
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException(postId) }
        if (post.isDeleted) {
            throw PostNotFoundException(postId)
        }
        return post
    }
}
