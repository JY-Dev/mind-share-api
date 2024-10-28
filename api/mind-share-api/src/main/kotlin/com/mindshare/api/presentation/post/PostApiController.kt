package com.mindshare.api.presentation.post

import com.mindshare.api.application.post.CreatePostUseCase
import com.mindshare.api.application.post.EditPostUseCase
import com.mindshare.api.presentation.post.model.request.CreatePostRequest
import com.mindshare.api.presentation.post.model.request.EditPostRequest
import com.mindshare.api.presentation.post.model.response.CreatePostResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class PostApiController(
    private val createPostUseCase: CreatePostUseCase,
    private val editPostUseCase: EditPostUseCase
) : PostApi {
    override fun createPost(request: CreatePostRequest, userId: Long): CreatePostResponse {
        val postId = createPostUseCase(request.title, request.content, userId)

        return CreatePostResponse(postId)
    }

    override fun editPost(request: EditPostRequest, postId: Long, userId: Long) {
        editPostUseCase(request.title, request.content, postId, userId)
    }
}