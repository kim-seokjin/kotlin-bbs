package com.example.board.like.exception

class DuplicateLikeException(
    postId: Long,
    likerName: String,
) : RuntimeException("이미 좋아요를 눌렀습니다. postId=$postId, likerName=$likerName")
