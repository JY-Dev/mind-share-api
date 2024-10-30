package com.mindshare.api.infra.post

import com.mindshare.domain.post.Post
import com.mindshare.domain.post.PostRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPostRepository : JpaRepository<Post, Long>, PostRepository {
}