package com.mindshare.api.infra.post

import com.mindshare.domain.post.InteractionType
import com.mindshare.domain.post.PostInteraction
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPostInteractionRepository : JpaRepository<PostInteraction, Long> {
    fun findByPostIdAndType(postId: Long, type: InteractionType): PostInteraction?
    fun deleteByPostId(postId: Long)
}