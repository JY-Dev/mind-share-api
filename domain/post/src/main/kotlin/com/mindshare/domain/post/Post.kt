package com.mindshare.domain.post

import com.mindshare.core.jpa.TimeAuditableEntity
import jakarta.persistence.*


@Entity
@Table(name = "POST")
class Post(

    title : String,

    content : String,

    @Column(name = "USER_ID")
    val userId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
) : TimeAuditableEntity() {

    @Column(name = "TITLE")
    var title = title
        private set

    @Column(name = "CONTENT")
    var content = content
        private set

    fun changePost(title: String, content: String) {
        this.title = title
        this.content = content
    }
}