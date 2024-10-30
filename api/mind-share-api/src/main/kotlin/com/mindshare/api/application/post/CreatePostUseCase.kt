package com.mindshare.api.application.post

import com.mindshare.domain.post.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatePostUseCase(
    private val postRepository: PostRepository,
    private val postInteractionRepository: PostInteractionRepository
) {

    @Transactional
    operator fun invoke(title: String, content: String, userId: Long): Long {
        val post = Post(title, content, userId)
        val savedPost = postRepository.save(post)

        InteractionType.entries.forEach { type ->
            postInteractionRepository.save(PostInteraction(postId = savedPost.id!!, type = type))
        }

        return savedPost.id!!
    }
}