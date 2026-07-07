package com.example.board.post.controller

import com.example.board.post.dto.PostCreateRequest
import com.example.board.post.dto.PostResponse
import com.example.board.post.dto.PostUpdateRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.board.post.service.PostService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "게시글", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
) {
    
    @Operation(summary = "게시글 생성")
    @PostMapping
    fun create(@Valid @RequestBody request: PostCreateRequest): ResponseEntity<PostResponse> {
    val post = postService.create(request)
    return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(post))
    }
    
    @Operation(summary = "게시글 수정")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long, 
        @Valid @RequestBody request: PostUpdateRequest
    ): ResponseEntity<PostResponse> {
        val post = postService.update(id, request)
        return ResponseEntity.status(HttpStatus.OK).body(PostResponse.from(post))
    }
    
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @RequestParam requesterName: String,
    ): ResponseEntity<Void> {
        postService.delete(id, requesterName)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
     
}