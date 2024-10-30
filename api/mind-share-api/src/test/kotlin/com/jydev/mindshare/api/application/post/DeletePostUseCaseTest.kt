package com.jydev.mindshare.api.application.post

import com.mindshare.api.application.post.DeletePostUseCase
import com.mindshare.api.application.post.error.PostPermissionException
import com.mindshare.domain.post.Post
import com.mindshare.domain.post.PostRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DeletePostUseCaseTest {

    private val postRepository = mock(PostRepository::class.java)
    private val deletePostUseCase = DeletePostUseCase(postRepository)

    @Test
    fun `게시글을 정상적으로 삭제할 수 있다`() {
        // given
        val postId = 1L
        val userId = 123L
        val post = Post(title = "기존 제목", content = "기존 내용", userId = userId)

        `when`(postRepository.findById(postId)).thenReturn(post)

        // when
        deletePostUseCase.invoke(postId, userId)
    }

    @Test
    fun `게시글이 존재하지 않을 때 NoSuchElementException을 발생시킨다`() {
        // given
        val postId = 1L
        val userId = 123L

        `when`(postRepository.findById(postId)).thenReturn(null)

        // when & then
        assertThrows(NoSuchElementException::class.java) {
            deletePostUseCase.invoke(postId, userId)
        }
    }

    @Test
    fun `권한이 없는 사용자가 게시글을 삭제하려고 하면 PostPermissionException을 발생시킨다`() {
        // given
        val postId = 1L
        val userId = 123L
        val otherUserId = 456L
        val post = Post(title = "기존 제목", content = "기존 내용", userId = otherUserId)

        `when`(postRepository.findById(postId)).thenReturn(post)

        // when & then
        assertThrows(PostPermissionException::class.java) {
            deletePostUseCase.invoke(postId, userId)
        }
    }
}