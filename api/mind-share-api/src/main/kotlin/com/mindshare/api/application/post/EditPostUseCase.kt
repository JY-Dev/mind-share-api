package com.mindshare.api.application.post

import com.mindshare.api.application.post.error.PostPermissionException
import com.mindshare.domain.post.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EditPostUseCase(
    private val postRepository: PostRepository
) {

    @Transactional
    operator fun invoke(title: String, content: String, postId: Long, userId: Long) {

        val post = postRepository.findById(postId)
            ?: throw NoSuchElementException("Post with id $postId not found")

        val isSameUser = post.userId == userId
        if(isSameUser.not()) {
            throw PostPermissionException("Post permission not allowed. Post : $postId")
        }

        post.changePost(title, content)
    }
}