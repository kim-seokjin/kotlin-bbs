package com.example.board.post.exception

class PostNotFoundException(postId: Long) : RuntimeException("게시글을 찾을 수 없습니다. id=$postId")
