package com.github.bumblebee202111.minusone.server.controller.cdn

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/cdn/media")
class MediaController(
    @Value("\${app.media.storage-path:minusone-media-default/originals}")
    private val storagePath: String
) {

    private val log = LoggerFactory.getLogger(MediaController::class.java)
    private lateinit var storageRoot: Path

    @PostConstruct
    fun init() {
        this.storageRoot = Paths.get(storagePath).toAbsolutePath()
        if (!Files.exists(storageRoot)) {
            try {
                Files.createDirectories(storageRoot)
                log.info("Created media storage directory at {}", storageRoot)
            } catch (e: Exception) {
                log.error("Failed to create media storage directory at {}", storageRoot, e)
            }
        } else {
            log.info("Using existing media storage directory at {}", storageRoot)
        }
    }

    @GetMapping("/{filename:.+}")
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val requestedFile = storageRoot.resolve(filename).normalize()

        
        if (!requestedFile.startsWith(storageRoot)) {
            log.warn("Blocked directory traversal attempt: {}", filename)
            return ResponseEntity.badRequest().build()
        }

        val resource: Resource = FileSystemResource(requestedFile)

        if (!resource.exists() || !resource.isReadable) {
            return ResponseEntity.notFound().build()
        }

        val contentType = when (requestedFile.fileName.toString().substringAfterLast('.', "").lowercase()) {
            "mp3" -> MediaType.valueOf("audio/mpeg")
            "flac" -> MediaType.valueOf("audio/flac")
            "jpg", "jpeg" -> MediaType.IMAGE_JPEG
            "png" -> MediaType.IMAGE_PNG
            else -> MediaType.APPLICATION_OCTET_STREAM
        }

        return ResponseEntity.ok()
            .contentType(contentType)
            .body(resource)
    }
}