package com.example.board.post.domain

import com.example.board.post.dto.PostSummaryResponse
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class PostQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : PostQueryRepository {
    override fun findPostSummaries(pageable: Pageable): Page<PostSummaryResponse> {
        val post = QPost.post

        val content =
            queryFactory
                .select(
                    Projections.constructor(
                        PostSummaryResponse::class.java,
                        post.id,
                        post.title,
                        post.authorName,
                        post.createdAt,
                    ),
                )
                .from(post)
                .where(post.deletedAt.isNull)
                .orderBy(post.createdAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val total =
            queryFactory
                .select(post.count())
                .from(post)
                .where(post.deletedAt.isNull)
                .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}
