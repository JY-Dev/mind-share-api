package com.mindshare.api.application.post

import com.mindshare.api.application.post.model.PostListModel
import com.mindshare.api.core.paging.PageInfo
import com.mindshare.api.core.paging.PageInfo.Companion.pagination
import com.mindshare.api.core.util.toPairPageToken
import com.mindshare.domain.post.Post
import com.mindshare.domain.post.QPost
import com.mindshare.domain.user.QUser
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.querydsl.QSort
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class PostFinder : QuerydslRepositorySupport(QPost::class.java) {

    fun searchWithPaging(
        keyword: String? = null,
        pageToken: String? = null,
        pageSize: Int = 20,
        order: PostOrder = PostOrder.POST_CREATION_TIME_DESC
    ): PageInfo<PostListModel> {

        val qPost = QPost.post
        val qUser = QUser.user

        return querydsl!!.createQuery<Post>()
            .select(listProjection(qPost, qUser))
            .from(qPost)
            .join(qUser).on(qPost.userId.eq(qUser.id))
            .condition(qPost, keyword)
            .cursorPaging(qPost, pageToken, order)
            .limit(pageSize.toLong().plus(1))
            .fetch()
            .pagination(pageSize, PostListModel::creationTime, PostListModel::postId)
    }

    private fun listProjection(post: QPost, user: QUser): Expression<PostListModel> {
        return Projections.constructor(
            PostListModel::class.java,
            post.id,
            post.title,
            user.nickname,
            post.creationTime
        )
    }

    private fun JPQLQuery<PostListModel>.condition(qPost: QPost, keyword: String?): JPQLQuery<PostListModel> {

        if (keyword.isNullOrBlank().not()) {
            val matchTitleOrContent: BooleanExpression = qPost.title.like("%$keyword%")
                .or(qPost.content.like("%$keyword%"))

            this.where(matchTitleOrContent)
        }

        return this
    }

    private fun JPQLQuery<PostListModel>.order(order: PostOrder): JPQLQuery<PostListModel> {
        return querydsl!!.applySorting(order.sort, this)
    }

    private fun JPQLQuery<PostListModel>.cursorPaging(qPost: QPost, pageToken: String?, order: PostOrder): JPQLQuery<PostListModel> {

        when (order) {
            PostOrder.POST_CREATION_TIME_DESC -> {
                pageToken?.toPairPageToken<Instant, Long>()
                    ?.let { (creationTime, postId) ->
                        val beforeCreationTime = qPost.creationTime.lt(creationTime)
                        val beforePostId = qPost.creationTime.eq(creationTime)
                            .and(qPost.id.lt(postId))
                        this.where(beforeCreationTime.or(beforePostId))
                    }
            }

            PostOrder.POST_CREATION_TIME_ASC -> {
                pageToken?.toPairPageToken<Instant, Long>()
                    ?.let { (creationTime, postId) ->
                        val afterCreationTime = qPost.creationTime.gt(creationTime)
                        val afterOrEqualCreationTimeAndId = qPost.creationTime.eq(creationTime)
                            .and(qPost.id.gt(postId))
                        this.where(afterCreationTime.or(afterOrEqualCreationTimeAndId))
                    }
            }
        }

        return this.order(order)
    }

    enum class PostOrder(description: String, val sort: Sort) {
        POST_CREATION_TIME_DESC(
            "게시글 생성순 (내림차순)",
            QSort.by(QPost.post.id.desc())
        ),
        POST_CREATION_TIME_ASC(
            "질병 이름 (오름차순)",
            QSort.by(QPost.post.creationTime.asc(), QPost.post.id.asc())
        );
    }
}