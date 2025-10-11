package com.github.bumblebee202111.minusone.server.dto.admin.response

import java.nio.file.Path
import java.time.Instant

data class AdminResourceDto(
    val filename: String,
    val size: Long, 
    val lastModified: Instant
) {
    companion object {
        fun from(path: Path): AdminResourceDto {
            return AdminResourceDto(
                filename = path.fileName.toString(),
                size = path.toFile().length(),
                lastModified = path.toFile().lastModified().let { Instant.ofEpochMilli(it) }
            )
        }
    }
}