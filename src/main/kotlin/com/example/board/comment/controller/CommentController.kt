package com.example.board.comment.controller

import com.example.board.comment.dto.CommentCreateRequest
import com.example.board.comment.dto.CommentResponse
import com.example.board.comment.dto.CommentUpdateRequest
import com.example.board.comment.service.CommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "댓글", description = "댓글 관련 API")
@RestController
@RequestMapping("/api")
class CommentController(
    private val commentService: CommentService,
) {
    @Operation(summary = "댓글 생성")
    @PostMapping("/posts/{postId}/comments")
    fun create(
        @PathVariable postId: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): ResponseEntity<CommentResponse> {
        val comment = commentService.create(postId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.from(comment))
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/comments/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: CommentUpdateRequest,
    ): ResponseEntity<CommentResponse> {
        val comment = commentService.update(id, request)
        return ResponseEntity.ok(CommentResponse.from(comment))
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{id}")
    fun delete(
        @PathVariable id: Long,
        @RequestParam requesterName: String,
    ): ResponseEntity<Void> {
        commentService.delete(id, requesterName)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "댓글 목록 조회")
    @GetMapping("/posts/{postId}/comments")
    fun getComments(
        @PathVariable postId: Long,
    ): ResponseEntity<List<CommentResponse>> {
        val comments = commentService.getComments(postId)
        return ResponseEntity.ok(comments.map { CommentResponse.from(it) })
    }
}
