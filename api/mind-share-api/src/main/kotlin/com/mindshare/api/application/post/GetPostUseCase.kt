package com.mindshare.api.application.post

import com.mindshare.api.application.post.model.PostDetailModel
import com.mindshare.domain.post.InteractionType
import org.springframework.stereotype.Service

@Service
class GetPostUseCase(
    private val postFinder: PostFinder,
    private val postInteractionCounter: PostInteractionCounter
) {

    operator fun invoke(postId: Long): Pair<PostDetailModel, Long> {
        val viewCount = postInteractionCounter.incrementCount(postId, InteractionType.VIEW_COUNT)
        val postDetail = postFinder.findPostDetail(postId) ?: throw NoSuchElementException("Post $postId not found")
        return postDetail to viewCount
    }
}