package com.github.bumblebee202111.minusone.server.repository
import com.github.bumblebee202111.minusone.server.entity.CommentThread
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentThreadRepository : JpaRepository<CommentThread, Long> {
    fun findByResourceId(resourceId: String): CommentThread?
}