package com.example.board.like.service

import com.example.board.like.domain.Like
import com.example.board.like.domain.LikeRepository
import com.example.board.like.exception.DuplicateLikeException
import com.example.board.like.exception.LikeNotFoundException
import com.example.board.post.domain.Post
import com.example.board.post.domain.PostRepository
import com.example.board.post.exception.PostNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun like(
        postId: Long,
        likerName: String,
    ): Like {
        val post = findActivePost(postId)
        if (likeRepository.existsByPostIdAndLikerName(post.id!!, likerName)) {
            throw DuplicateLikeException(postId, likerName)
        }
        return likeRepository.save(Like.create(post.id!!, likerName))
    }

    @Transactional
    fun unlike(
        postId: Long,
        likerName: String,
    ) {
        findActivePost(postId)
        val like =
            likeRepository.findByPostIdAndLikerName(postId, likerName)
                ?: throw LikeNotFoundException(postId, likerName)
        likeRepository.delete(like)
    }

    @Transactional(readOnly = true)
    fun countLikes(postId: Long): Long {
        findActivePost(postId)
        return likeRepository.countByPostId(postId)
    }

    @Transactional(readOnly = true)
    fun isLiked(
        postId: Long,
        likerName: String,
    ): Boolean {
        findActivePost(postId)
        return likeRepository.existsByPostIdAndLikerName(postId, likerName)
    }

    private fun findActivePost(postId: Long): Post {
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException(postId) }
        if (post.isDeleted) {
            throw PostNotFoundException(postId)
        }
        return post
    }
}
