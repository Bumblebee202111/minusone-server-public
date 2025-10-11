package com.github.bumblebee202111.minusone.server.dto.api.response

data class CommentInfoItemDto(
    val latestLikedUsers: Any? = null,    
    val liked: Boolean = false,           
    val comments: Any? = null,            
    val resourceType: Int = 4,            
    val resourceId: Long,                 
    val commentUpgraded: Boolean = false, 
    val musicianSaidCount: Int = 0,       
    val commentCountDesc: String,
    val likedCount: Long = 0,             
    val shareCount: Long = 0,             
    val commentCount: Long,
    val threadId: String
)