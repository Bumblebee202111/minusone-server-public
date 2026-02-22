package com.github.bumblebee202111.minusone.server.controller.api

import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.resolver.JsonQueryParam
import com.github.bumblebee202111.minusone.server.service.api.ResourceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
@Validated
@Tag(name = "Resource", description = "Resource related APIs")
class ResourceController(
    private val resourceService: ResourceService
) {

    @GetMapping("/resource/commentInfo/list")
    @Operation(summary = "Get comment info list", description = "Gets comment info list by resource IDs")
    fun getCommentInfoResourceList(
        @JsonQueryParam @NotEmpty resourceIds: List<Long>, @RequestParam resourceType: Int
    ): ApiResponse {
        val commentInfoList = resourceService.getCommentInfoList(resourceIds)
        return ApiResponse.success(data = commentInfoList)
    }

    @GetMapping("/v2/resource/comments")
    @Operation(summary = "Get resource comments", description = "Gets resource comments by thread ID")
    fun getV2ResourceComments(@RequestParam @NotBlank threadId: String): ApiResponse {
        val comments = resourceService.getComments(threadId)
        return ApiResponse.success(data = comments)
    }
}
