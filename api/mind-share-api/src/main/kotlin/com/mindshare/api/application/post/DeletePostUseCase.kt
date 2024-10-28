package com.mindshare.api.application.post

import com.mindshare.api.application.post.error.PostPermissionException
import com.mindshare.domain.post.Post
import com.mindshare.domain.post.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeletePostUseCase(
    private val postRepository: PostRepository
) {

    @Transactional
    operator fun invoke(postId : Long, userId : Long) {
        val post = postRepository.findById(postId)
            ?: throw NoSuchElementException("Post with id $postId not found")

        val isSameUser = post.userId == userId
        if(isSameUser.not()) {
            throw PostPermissionException("Post permission not allowed. Post : $postId")
        }

        postRepository.delete(post)
    }
}