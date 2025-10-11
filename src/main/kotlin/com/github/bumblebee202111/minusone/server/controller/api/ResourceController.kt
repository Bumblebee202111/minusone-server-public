package com.github.bumblebee202111.minusone.server.controller.api

import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.resolver.JsonQueryParam
import com.github.bumblebee202111.minusone.server.service.api.MusicService 
import com.github.bumblebee202111.minusone.server.service.api.ResourceService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
@Validated
class ResourceController(
    private val resourceService: ResourceService
) {

    @RequestMapping("/resource/commentInfo/list")
    fun getCommentInfoResourceList(
        @JsonQueryParam @NotEmpty resourceIds: List<Long>, @RequestParam resourceType: Int
    ): ApiResponse {
        val commentInfoList = resourceService.getCommentInfoList(resourceIds)
        return ApiResponse.success(data = commentInfoList)
    }

    @RequestMapping("/v2/resource/comments")
    fun getV2ResourceComments(@RequestParam @NotBlank threadId: String): ApiResponse {
        val comments = resourceService.getComments(threadId)
        return ApiResponse.success(data = comments)
    }
}