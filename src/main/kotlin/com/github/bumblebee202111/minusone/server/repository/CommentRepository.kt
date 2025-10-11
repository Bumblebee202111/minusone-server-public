package com.github.bumblebee202111.minusone.server.repository
import com.github.bumblebee202111.minusone.server.entity.Comment
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

data class ThreadCommentCount(val threadId: Long, val count: Long)

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.user u WHERE c.thread.id = :threadId ORDER BY c.likedCount DESC")
    fun findByThreadId(threadId: Long, pageable: Pageable): List<Comment>

    fun countByThreadId(threadId: Long): Long
    @Query("SELECT new com.github.bumblebee202111.minusone.server.repository.ThreadCommentCount(c.thread.id, COUNT(c.id)) FROM Comment c WHERE c.thread.id IN :threadIds GROUP BY c.thread.id")
    fun countCommentsByThreadIds(threadIds: List<Long>): List<ThreadCommentCount>
}