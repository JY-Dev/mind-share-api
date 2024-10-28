package com.mindshare.api.presentation.post

import com.mindshare.api.application.post.CreatePostUseCase
import com.mindshare.api.application.post.DeletePostUseCase
import com.mindshare.api.application.post.EditPostUseCase
import com.mindshare.api.application.post.PostFinder
import com.mindshare.api.core.util.toKstLocalDateTime
import com.mindshare.api.core.web.PagingResponse
import com.mindshare.api.presentation.post.model.request.CreatePostRequest
import com.mindshare.api.presentation.post.model.request.EditPostRequest
import com.mindshare.api.presentation.post.model.response.CreatePostResponse
import com.mindshare.api.presentation.post.model.response.ListPostResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class PostApiController(
    private val createPostUseCase: CreatePostUseCase,
    private val editPostUseCase: EditPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val postFinder: PostFinder
) : PostApi {
    override fun createPost(request: CreatePostRequest, userId: Long): CreatePostResponse {
        val postId = createPostUseCase(request.title, request.content, userId)

        return CreatePostResponse(postId)
    }

    override fun editPost(request: EditPostRequest, postId: Long, userId: Long) {
        editPostUseCase(request.title, request.content, postId, userId)
    }

    override fun deletePost(postId: Long, userId: Long) {
        deletePostUseCase(postId, userId)
    }

    override fun listPost(keyword: String?, pageToken: String?, pageSize: Int): ListPostResponse {
        val page = postFinder.searchWithPaging(keyword, pageToken, pageSize)
        val content = page.data
            .map { item ->
                ListPostResponse.ListPostItemResponse(item.postId, item.title, item.nickname, item.creationTime.toKstLocalDateTime())
            }
        val pagingResponse = PagingResponse(
            pageToken = page.pageToken,
            hasNext = page.hasNext
        )

        return ListPostResponse(
            contents = content,
            paging = pagingResponse
        )
    }

}