package com.github.bumblebee202111.minusone.server.service.admin

import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminResourceDto
import com.github.bumblebee202111.minusone.server.exception.admin.DataConflictException
import com.github.bumblebee202111.minusone.server.exception.admin.ResourceNotFoundException
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.streams.toList

@Service
class AdminResourceService(
    @Value("\${app.media.storage-path:minusone-media/originals}")
    private val storagePath: String
) {
    private lateinit var storageRoot: Path

    @PostConstruct
    fun init() {
        this.storageRoot = Paths.get(storagePath).toAbsolutePath().normalize()
        if (!Files.exists(storageRoot)) {
            Files.createDirectories(storageRoot)
        }
    }

    
    fun listAllFiles(): List<AdminResourceDto> {
        return Files.walk(this.storageRoot, 1)
            .filter { path -> Files.isRegularFile(path) }
            .map { path -> AdminResourceDto.from(path) }
            .toList()
            .sortedByDescending { it.lastModified }
    }

    
    fun storeFile(file: MultipartFile): AdminResourceDto {
        val originalFilename = StringUtils.cleanPath(file.originalFilename ?: "unnamed-file")
        if (originalFilename.contains("..")) {
            throw IllegalArgumentException("Filename contains invalid path sequence: $originalFilename")
        }

        val destinationFile = this.storageRoot.resolve(originalFilename)

        if (Files.exists(destinationFile)) {
            throw DataConflictException("File with name '$originalFilename' already exists.")
        }

        Files.copy(file.inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)

        return AdminResourceDto.from(destinationFile)
    }

    
    fun deleteFile(filename: String) {
        if (filename.contains("..")) {
            throw IllegalArgumentException("Filename contains invalid path sequence.")
        }

        val fileToDelete = this.storageRoot.resolve(filename)

        if (!Files.exists(fileToDelete) || !fileToDelete.startsWith(storageRoot)) {
            throw ResourceNotFoundException("File not found: $filename")
        }

        Files.delete(fileToDelete)
    }
}