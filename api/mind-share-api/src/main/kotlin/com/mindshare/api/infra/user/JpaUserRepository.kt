package com.mindshare.api.infra.user

import com.mindshare.domain.user.User
import com.mindshare.domain.user.UserRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<User, Long>, UserRepository {
}