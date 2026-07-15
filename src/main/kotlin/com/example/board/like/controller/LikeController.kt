package com.example.board.like.controller

import com.example.board.like.dto.LikeCountResponse
import com.example.board.like.dto.LikeCreateRequest
import com.example.board.like.dto.LikeResponse
import com.example.board.like.dto.LikeStatusResponse
import com.example.board.like.service.LikeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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

@Tag(name = "좋아요", description = "좋아요 관련 API")
@RestController
@RequestMapping("/api/posts/{postId}/likes")
class LikeController(
    private val likeService: LikeService,
) {
    @Operation(summary = "좋아요 누르기")
    @PostMapping
    fun like(
        @PathVariable postId: Long,
        @Valid @RequestBody request: LikeCreateRequest,
    ): ResponseEntity<LikeResponse> {
        val like = likeService.like(postId, request.likerName)
        return ResponseEntity.status(HttpStatus.CREATED).body(LikeResponse.from(like))
    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping
    fun unlike(
        @PathVariable postId: Long,
        @RequestParam likerName: String,
    ): ResponseEntity<Void> {
        likeService.unlike(postId, likerName)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "좋아요 개수 조회")
    @GetMapping("/count")
    fun countLikes(
        @PathVariable postId: Long,
    ): ResponseEntity<LikeCountResponse> {
        val count = likeService.countLikes(postId)
        return ResponseEntity.ok(LikeCountResponse(count))
    }

    @Operation(summary = "좋아요 여부 조회")
    @GetMapping("/me")
    fun isLiked(
        @PathVariable postId: Long,
        @RequestParam likerName: String,
    ): ResponseEntity<LikeStatusResponse> {
        val isLiked = likeService.isLiked(postId, likerName)
        return ResponseEntity.ok(LikeStatusResponse(isLiked))
    }
}
