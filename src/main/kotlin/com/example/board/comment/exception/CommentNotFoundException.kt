package com.example.board.comment.exception

class CommentNotFoundException(commentId: Long) : RuntimeException("댓글을 찾을 수 없습니다. id=$commentId")
