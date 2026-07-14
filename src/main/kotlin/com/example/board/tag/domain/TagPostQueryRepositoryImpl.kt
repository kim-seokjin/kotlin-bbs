package com.example.board.tag.domain

import com.example.board.post.domain.QPost
import com.example.board.post.dto.PostSummaryResponse
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class TagPostQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : TagPostQueryRepository {
    override fun findPostsByTagName(
        tagName: String,
        pageable: Pageable,
    ): Page<PostSummaryResponse> {
        val post = QPost.post
        val postTag = QPostTag.postTag
        val tag = QTag.tag

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
                .from(postTag)
                .join(post).on(post.id.eq(postTag.postId))
                .join(tag).on(tag.id.eq(postTag.tagId))
                .where(tag.name.eq(tagName), post.deletedAt.isNull)
                .orderBy(post.createdAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val total =
            queryFactory
                .select(postTag.count())
                .from(postTag)
                .join(post).on(post.id.eq(postTag.postId))
                .join(tag).on(tag.id.eq(postTag.tagId))
                .where(tag.name.eq(tagName), post.deletedAt.isNull)
                .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}
