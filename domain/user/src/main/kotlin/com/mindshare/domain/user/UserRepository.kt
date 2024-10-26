package com.mindshare.domain.user

interface UserRepository {
    fun existNickname(nickname: String): Boolean
    fun findById(id: Long): User?
}