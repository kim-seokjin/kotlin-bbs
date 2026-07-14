package com.example.board.tag.controller

import com.example.board.post.dto.PostSummaryResponse
import com.example.board.tag.dto.TagAttachRequest
import com.example.board.tag.dto.TagResponse
import com.example.board.tag.service.TagService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "태그", description = "태그 관련 API")
@RestController
@RequestMapping("/api")
class TagController(
    private val tagService: TagService,
) {
    @Operation(summary = "게시글에 태그 추가")
    @PostMapping("/posts/{postId}/tags")
    fun attachTags(
        @PathVariable postId: Long,
        @Valid @RequestBody request: TagAttachRequest,
    ): ResponseEntity<List<TagResponse>> {
        val tags = tagService.attachTags(postId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(tags.map { TagResponse.from(it) })
    }

    @Operation(summary = "게시글에서 태그 제거")
    @DeleteMapping("/posts/{postId}/tags/{tagId}")
    fun detachTag(
        @PathVariable postId: Long,
        @PathVariable tagId: Long,
        @RequestParam requesterName: String,
    ): ResponseEntity<Void> {
        tagService.detachTag(postId, tagId, requesterName)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "게시글의 태그 목록 조회")
    @GetMapping("/posts/{postId}/tags")
    fun getTagsOfPost(
        @PathVariable postId: Long,
    ): ResponseEntity<List<TagResponse>> {
        val tags = tagService.getTagsOfPost(postId)
        return ResponseEntity.ok(tags.map { TagResponse.from(it) })
    }

    @Operation(summary = "태그명 검색 (자동완성)")
    @GetMapping("/tags")
    fun searchTags(
        @RequestParam keyword: String,
    ): ResponseEntity<List<TagResponse>> {
        val tags = tagService.searchTags(keyword)
        return ResponseEntity.ok(tags.map { TagResponse.from(it) })
    }

    @GetMapping("/tags/{tagName}/posts")
    fun getPostsByTag(
        @PathVariable tagName: String,
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<PostSummaryResponse>> {
        return ResponseEntity.ok(tagService.getPostsByTag(tagName, pageable))
    }
}
