package com.mindshare.domain.post

interface PostRepository {
    fun findById(id: Long): Post
    fun delete(post: Post)
    fun save(post: Post): Post
}