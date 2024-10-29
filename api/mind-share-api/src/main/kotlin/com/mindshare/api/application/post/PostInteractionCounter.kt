package com.mindshare.api.application.post

import com.mindshare.domain.post.InteractionType

interface PostInteractionCounter {
    fun incrementCount(postId: Long, type: InteractionType): Long
    fun decrementCount(postId: Long, type: InteractionType): Long
    fun getCount(postId: Long, type: InteractionType): Long
}