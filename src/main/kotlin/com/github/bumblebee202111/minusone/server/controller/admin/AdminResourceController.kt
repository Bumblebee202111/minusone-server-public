package com.github.bumblebee202111.minusone.server.controller.admin

import com.github.bumblebee202111.minusone.server.service.admin.AdminResourceService
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminResourceDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

@RestController
@RequestMapping("/admin/resources")
@Validated
@Tag(name = "Admin Resource", description = "Admin Resource related APIs")
class AdminResourceController(
    private val resourceService: AdminResourceService
) {

    
    @GetMapping
    @Operation(summary = "List resources", description = "Lists all available media files")
    fun listResources(): List<AdminResourceDto> {
        return resourceService.listAllFiles()
    }

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload resource", description = "Uploads a new media file")
    fun uploadResource(@RequestParam("file") file: MultipartFile): AdminResourceDto {
        return resourceService.storeFile(file)
    }

    
    @DeleteMapping("/{filename:.+}") 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete resource", description = "Deletes a media file")
    fun deleteResource(@PathVariable filename: String) {
        resourceService.deleteFile(filename)
    }
}
