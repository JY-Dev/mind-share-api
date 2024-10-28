package com.mindshare.api.application.post.model

import java.time.Instant

data class PostListModel(
    val postId: Long,
    val title: String,
    val nickname: String,
    val creationTime: Instant,
)