package com.mindshare.api.infra.post

import com.mindshare.domain.post.InteractionType
import com.mindshare.domain.post.PostInteraction
import com.mindshare.domain.post.PostInteractionRepository
import org.springframework.stereotype.Repository

@Repository
class CustomPostInteractionRepository(
    private val jpaPostInteractionRepository: JpaPostInteractionRepository
) : PostInteractionRepository {
    override fun findByPostIdAndType(postId: Long, type: InteractionType): PostInteraction? =
        jpaPostInteractionRepository.findByPostIdAndType(postId, type)

    override fun save(postInteraction: PostInteraction): PostInteraction =
        jpaPostInteractionRepository.save(postInteraction)

    override fun deleteByPostId(postId: Long) =
        jpaPostInteractionRepository.deleteByPostId(postId)
}