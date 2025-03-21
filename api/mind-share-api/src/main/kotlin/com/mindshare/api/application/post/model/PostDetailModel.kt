package com.mindshare.api.application.post.model

import java.time.Instant

data class PostDetailModel(
    val title: String,
    val content: String,
    val nickname: String?,
    val creationTime: Instant,
)
