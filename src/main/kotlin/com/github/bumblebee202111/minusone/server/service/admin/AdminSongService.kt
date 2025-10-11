package com.github.bumblebee202111.minusone.server.service.admin

import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminSongUpdateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminSongCreateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.request.toEntity
import com.github.bumblebee202111.minusone.server.dto.admin.request.updateEntity
import com.github.bumblebee202111.minusone.server.exception.admin.SongWithIdAlreadyExistsAdminException
import com.github.bumblebee202111.minusone.server.exception.admin.SongWithIdDoesNotExistAdminException
import com.github.bumblebee202111.minusone.server.repository.SongRepository
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminSongDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.PageResponseDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.toAdminSongDto
import com.github.bumblebee202111.minusone.server.exception.admin.ResourceNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminSongService(private val songRepository: SongRepository) {

    @Transactional
    fun createSong(request: AdminSongCreateRequest): AdminSongDto {
        val newSong = request.toEntity()
        val savedSong = songRepository.save(newSong)
        return savedSong.toAdminSongDto()
    }

    @Transactional(readOnly = true)
    fun listSongs(page: Int, size: Int): PageResponseDto<AdminSongDto> {
        val pageable = PageRequest.of(page, size, Sort.by("id").ascending())
        val songPage = songRepository.findAll(pageable)
        val dtoPage = songPage.map { it.toAdminSongDto() }
        return PageResponseDto.from(dtoPage)
    }

    @Transactional(readOnly = true)
    fun getSong(songId: Long): AdminSongDto {
        return songRepository.findById(songId)
            .orElseThrow { ResourceNotFoundException("Song not found with ID: $songId") }
            .toAdminSongDto()
    }

    @Transactional
    fun updateSong(songId: Long, request: AdminSongUpdateRequest): AdminSongDto {
        val song = songRepository.findById(songId)
            .orElseThrow { ResourceNotFoundException("Song not found with ID: $songId") }

        val updatedSong = request.updateEntity(song)

        songRepository.save(updatedSong)

        return updatedSong.toAdminSongDto()
    }

    @Transactional
    fun deleteSong(songId: Long) {
        if (!songRepository.existsById(songId)) {
            return
        }
        songRepository.deleteById(songId)
    }
}