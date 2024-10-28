package com.mindshare.api.presentation.post

import com.mindshare.api.application.post.CreatePostUseCase
import com.mindshare.api.presentation.post.model.request.CreatePostRequest
import com.mindshare.api.presentation.post.model.response.CreatePostResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class PostApiController(
    private val createPostUseCase: CreatePostUseCase,
) : PostApi {
    override fun createPost(request: CreatePostRequest, userId : Long): CreatePostResponse {
        val postId = createPostUseCase(request.title, request.content, userId)

        return CreatePostResponse(postId)
    }
}