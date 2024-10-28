package com.mindshare.domain.post

interface PostInteractionRepository {
    fun findByPostIdAndType(postId: Long, type: InteractionType): PostInteraction?
    fun save(postInteraction: PostInteraction): PostInteraction
    fun deleteByPostId(postId: Long)
}