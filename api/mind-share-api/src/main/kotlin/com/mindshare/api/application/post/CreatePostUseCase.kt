package com.mindshare.api.application.post

import com.mindshare.domain.post.Post
import com.mindshare.domain.post.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatePostUseCase(
    private val postRepository: PostRepository
) {

    @Transactional
    operator fun invoke(title : String, content : String, userId : Long) : Long {
        val post = Post(title, content, userId)
        val savedPost = postRepository.save(post)
        return savedPost.id!!
    }
}