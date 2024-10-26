package com.mindshare.domain.user

interface UserRepository {
    fun save(user: User) : User
    fun delete(user: User)
    fun existsByNickname(nickname: String): Boolean
    fun findById(id: Long): User?
}