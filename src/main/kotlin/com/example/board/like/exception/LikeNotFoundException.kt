package com.example.board.like.exception

class LikeNotFoundException(
    postId: Long,
    likerName: String,
) : RuntimeException("좋아요를 찾을 수 없습니다. postId=$postId, likerName=$likerName")
