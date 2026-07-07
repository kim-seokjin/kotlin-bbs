package com.example.board.post.service

import com.example.board.post.domain.Post
import com.example.board.post.domain.PostRepository
import com.example.board.post.dto.PostCreateRequest
import com.example.board.post.dto.PostUpdateRequest
import com.example.board.post.exception.PostNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
) {
    @Transactional
    fun create(request: PostCreateRequest): Post {
        val post = Post.create(request.title, request.content, request.authorName)
        return postRepository.save(post)
    }
    
    @Transactional
    fun update(postId: Long, request: PostUpdateRequest): Post {
        val post = findActivePost(postId)
        post.update(request.title, request.content, request.requesterName)
        return post
    }
    
    @Transactional
    fun delete(postId: Long, requesterName: String) {
        val post = findActivePost(postId)
        post.delete(requesterName)
    }
    
    private fun findActivePost(postId: Long): Post {
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException(postId) }
        if (post.isDeleted) {
            throw PostNotFoundException(postId)
        }
        return post
    }
}