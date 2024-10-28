package com.mindshare.domain.post

import jakarta.persistence.*

@Table(name = "POST_INTERACTION")
@Entity
class PostInteraction(

    @Column(name = "POST_ID")
    val postId: Long,

    count : Long = 0L,

    type: InteractionType = InteractionType.VIEW_COUNT,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
) {

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    var type : InteractionType = type
        private set

    @Column(name = "COUNT")
    var count : Long = count
        private set

    fun updateCount(count: Long) {
        this.count = count
    }
}