package com.jydev.mindshare.api.application.post

import com.mindshare.api.application.post.EditPostUseCase
import com.mindshare.api.application.post.error.PostPermissionException
import com.mindshare.domain.post.Post
import com.mindshare.domain.post.PostRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.Test

class EditPostUseCaseTest {

    private val postRepository = mock(PostRepository::class.java)
    private val editPostUseCase = EditPostUseCase(postRepository)

    @Test
    fun `게시글을 정상적으로 수정할 수 있다`() {
        // given
        val postId = 1L
        val userId = 123L
        val title = "새로운 제목"
        val content = "새로운 내용"
        val existingPost = Post(title = "기존 제목", content = "기존 내용", userId = userId)

        `when`(postRepository.findById(postId)).thenReturn(existingPost)

        // when
        editPostUseCase.invoke(title, content, postId, userId)

        // then
        assertEquals(title, existingPost.title)
        assertEquals(content, existingPost.content)
    }

    @Test
    fun `게시글이 존재하지 않을 때 NoSuchElementException을 발생시킨다`() {
        // given
        val postId = 1L
        val userId = 123L

        `when`(postRepository.findById(postId)).thenReturn(null)

        // when & then
        val exception = assertThrows(NoSuchElementException::class.java) {
            editPostUseCase.invoke("새로운 제목", "새로운 내용", postId, userId)
        }
        assertEquals("Post with id $postId not found", exception.message)
    }

    @Test
    fun `권한이 없는 사용자가 게시글을 수정하려고 하면 PostPermissionException을 발생시킨다`() {
        // given
        val postId = 1L
        val userId = 123L
        val otherUserId = 456L
        val existingPost = Post(title = "기존 제목", content = "기존 내용", userId = otherUserId)

        `when`(postRepository.findById(postId)).thenReturn(existingPost)

        // when & then
        val exception = assertThrows(PostPermissionException::class.java) {
            editPostUseCase.invoke("새로운 제목", "새로운 내용", postId, userId)
        }
    }
}