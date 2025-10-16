package com.github.bumblebee202111.minusone.server.controller.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes
import com.github.bumblebee202111.minusone.server.constant.api.ApiConstants
import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.service.api.AuthService
import com.github.bumblebee202111.minusone.server.service.api.MusicService
import com.github.bumblebee202111.minusone.server.service.api.UserMusicService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [MusicController::class])
@AutoConfigureMockMvc(addFilters = false)
class MusicControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var musicService: MusicService

    @MockBean
    private lateinit var userMusicService: UserMusicService

    @MockBean
    private lateinit var authService: AuthService

    @Test
    fun `likeSong should return 301 error when user is not authenticated`() {
        mockMvc.get("/api/song/like?like=true&trackId=101")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.code") { value(ApiCodes.CODE_301_LOGOUT) }
                jsonPath("$.msg") { value("系统错误") }
            }
    }

    @Test
    fun `likeSong should return success when user is authenticated`() {
        val userAccount = Account(id = 1L, userName = "test", phone = "123", password = "abc")
        val trackIdToLike = 101L
        val expectedPlaylistId = 99L
        whenever(userMusicService.likeSong(userAccount, trackIdToLike, true)).thenReturn(expectedPlaylistId)

        mockMvc.get("/api/song/like?like=true&trackId=$trackIdToLike") {
            requestAttr(ApiConstants.ATTR_USER_ACCOUNT, userAccount)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.code") { value(200) }
            jsonPath("$.playlistId") { value(expectedPlaylistId) }
        }
    }
}