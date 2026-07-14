package com.example.board.common.exception

import com.example.board.comment.exception.CommentNotFoundException
import com.example.board.post.exception.PostNotFoundException
import com.example.board.tag.exception.TagNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(val message: String)

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(PostNotFoundException::class)
    fun handleNotFound(ex: PostNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(ex.message ?: "찾을 수 없습니다."))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handlebadRequest(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(e.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(e: ForbiddenException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(e.message ?: "권한이 없습니다."))
    }

    @ExceptionHandler(CommentNotFoundException::class)
    fun handleCommentNotFound(ex: CommentNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(ex.message ?: "찾을 수 없습니다."))
    }

    @ExceptionHandler(TagNotFoundException::class)
    fun handleTagNotFound(ex: TagNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(ex.message ?: "찾을 수 없습니다."))
    }
}
