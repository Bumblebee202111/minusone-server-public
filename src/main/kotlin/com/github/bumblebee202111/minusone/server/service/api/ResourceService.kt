package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiResourceConstants
import com.github.bumblebee202111.minusone.server.dto.api.response.CommentInfoItemDto
import com.github.bumblebee202111.minusone.server.dto.api.response.CommentResponseDto
import com.github.bumblebee202111.minusone.server.dto.api.response.SortTypeDto
import com.github.bumblebee202111.minusone.server.dto.api.response.toDto
import com.github.bumblebee202111.minusone.server.repository.CommentRepository
import com.github.bumblebee202111.minusone.server.repository.CommentThreadRepository
import com.github.bumblebee202111.minusone.server.repository.ProfileRepository
import com.github.bumblebee202111.minusone.server.repository.SongRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ResourceService(
    private val commentThreadRepository: CommentThreadRepository,
    private val commentRepository: CommentRepository,
    private val songRepository: SongRepository,
    private val profileRepository: ProfileRepository
) {

    fun getCommentInfoList(resourceIds: List<Long>): List<CommentInfoItemDto> {
        if (resourceIds.isEmpty()) {
            return emptyList()
        }

        val songs = songRepository.findAllById(resourceIds)
        val songsById = songs.associateBy { it.id }

        val threadIds = songs.mapNotNull { it.commentThread?.id }

        val countsByThreadId = if (threadIds.isNotEmpty()) {
            commentRepository.countCommentsByThreadIds(threadIds).associateBy({ it.threadId }, { it.count })
        } else {
            emptyMap()
        }

        return resourceIds.map { requestedId ->
            val song = songsById[requestedId]
            val thread = song?.commentThread
            val count = thread?.id?.let { countsByThreadId[it] } ?: 0L

            CommentInfoItemDto(
                resourceId = requestedId,
                threadId = thread?.resourceId ?: "${ApiResourceConstants.THREAD_PREFIX_SONG}$requestedId",
                commentCount = count,
                commentCountDesc = if (count > 0) count.toString() else ""
            )
        }

    }

    fun getComments(threadId: String): CommentResponseDto {
        val thread = commentThreadRepository.findByResourceId(threadId)
            ?: return createEmptyCommentResponse()

        val pageable = PageRequest.of(0, 20)
        val comments = commentRepository.findByThreadId(thread.id, pageable)
        val totalCount = commentRepository.countByThreadId(thread.id)

        if (comments.isEmpty()) {
            return createEmptyCommentResponse(totalCount)
        }

        val userIds = comments.map { it.user.id }.distinct()

        val profilesById = profileRepository.findAllByUserIdIn(userIds).associateBy { it.userId }

        val commentDtos = comments.map { comment ->
            val profile = profilesById[comment.user.id]
                ?: throw IllegalStateException("Data integrity issue: Profile not found for user ${comment.user.id}")
            comment.toDto(profile)
        }

        return CommentResponseDto(
            commentsTitle = "热门评论",
            totalCount = totalCount,
            hasMore = totalCount > commentDtos.size,
            comments = commentDtos,
            sortTypeList = createSortTypeList(),
            cursor = "placeholder_cursor",
            sortType = 2
        )
    }

    private fun createEmptyCommentResponse(total: Long = 0): CommentResponseDto {
        return CommentResponseDto(
            commentsTitle = if (total > 0) "热门评论" else "全部评论",
            totalCount = total,
            hasMore = false,
            comments = emptyList(),
            sortTypeList = createSortTypeList(),
            cursor = if (total > 0) "placeholder_cursor" else "0",
            sortType = if (total > 0) 2 else 99
        )
    }

    private fun createSortTypeList(): List<SortTypeDto> {
        return listOf(
            SortTypeDto(99, "按推荐排序"),
            SortTypeDto(2, "按热度排序"),
            SortTypeDto(3, "按时间排序")
        )
    }
}