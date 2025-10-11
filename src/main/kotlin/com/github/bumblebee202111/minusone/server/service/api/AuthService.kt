package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.dto.api.internal.CellphoneLoginRequest
import com.github.bumblebee202111.minusone.server.dto.api.internal.RegisterRequest
import com.github.bumblebee202111.minusone.server.dto.api.response.PhoneAccountInfoDto
import com.github.bumblebee202111.minusone.server.exception.api.LoginCellphoneNotRegisteredException
import com.github.bumblebee202111.minusone.server.exception.api.LoginWrongIdOrPasswordException
import com.github.bumblebee202111.minusone.server.exception.api.PhoneOrUserAlreadyRegisteredException
import com.github.bumblebee202111.minusone.server.repository.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import kotlin.random.Random

@Service
class AuthService(
    private val accountRepository: AccountRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val anonimousUserRepository: AnonimousUserRepository,
    private val playlistRepository: PlaylistRepository,
    private val profileRepository: ProfileRepository
) {
    private val log = LoggerFactory.getLogger(AuthService::class.java)
    private val tokenValidityDurationSeconds: Long = 30 * 24 * 60 * 60

    private fun generateToken(): String {
        var remainingCharCount = Random.nextInt(50, 500) * 2

        return buildString(remainingCharCount) {
            while (remainingCharCount > 0) {
                val uuidSegment = UUID.randomUUID().toString().replace("-", "")
                val segmentToTake = uuidSegment.take(minOf(uuidSegment.length, remainingCharCount))
                append(segmentToTake)
                remainingCharCount -= segmentToTake.length
            }
        }.uppercase(Locale.getDefault())
    }


    @Transactional
    fun issueTokenForAccount(account: com.github.bumblebee202111.minusone.server.entity.Account): String {
        val tokenValue = generateToken()
        val now = Instant.now()
        val expiresAt = now.plusSeconds(tokenValidityDurationSeconds)

        val authToken = com.github.bumblebee202111.minusone.server.entity.AuthToken(
            token = tokenValue,
            account = account,
            expiresAt = expiresAt
        )
        authTokenRepository.save(authToken)

        log.info("Issued MUSIC_U token {} for accountId {}", tokenValue, account.id)
        return tokenValue
    }

    @Transactional(readOnly = true)
    fun validateTokenAndGetAccount(token: String): com.github.bumblebee202111.minusone.server.entity.Account? {
        if (token.isBlank()) return null

        val authToken: com.github.bumblebee202111.minusone.server.entity.AuthToken? = authTokenRepository.findByTokenAndExpiresAtAfter(token, Instant.now())

        if (authToken == null) {
            log.warn("Token {} not found or has expired.", token)
            return null
        }

        val account = authToken.account
        log.debug("Token {} validated for accountId {}.", token, account.id)
        return account
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    fun cleanupExpiredAuthTokens() {
        val now = Instant.now()
        log.info("Starting cleanup of expired auth tokens before {}", now)
        val deletedCount = authTokenRepository.deleteAllByExpiresAtBefore(now)
        log.info("Finished cleanup: {} expired auth tokens deleted.", deletedCount)
    }

    fun registerAnonimously(username: String): com.github.bumblebee202111.minusone.server.entity.RegisterAnonimousResult {

        val user = anonimousUserRepository.save(
            com.github.bumblebee202111.minusone.server.entity.AnonimousUser(
                userId = Random.nextLong(100000000, 9999999999),
                createTime = System.currentTimeMillis()
            )
        )
        return com.github.bumblebee202111.minusone.server.entity.RegisterAnonimousResult(
            userId = user.userId,
            createTime = user.createTime
        )
    }

    @Transactional
    fun cellphoneLogin(request: CellphoneLoginRequest): Pair<com.github.bumblebee202111.minusone.server.entity.Account, String> {

        val userName = "1_${request.phone}"

        if (!accountRepository.existsByUserName(userName)) throw LoginCellphoneNotRegisteredException()
        val account = accountRepository.findByUserNameAndPassword(userName, request.password)
            ?: throw LoginWrongIdOrPasswordException()

        val tokenString = issueTokenForAccount(account)

        return Pair(account, tokenString)
    }

    @Transactional
    fun registerByCellphone(
        registerRequest: RegisterRequest
    ): Pair<com.github.bumblebee202111.minusone.server.entity.Account, String> {
        val phone = registerRequest.phone
        val userName = "1_$phone"

        if (accountRepository.existsByPhone(phone) || accountRepository.existsByUserName(userName)) {
            log.warn("Registration failed: Phone {} or username {} already exists.", phone, userName)
            throw PhoneOrUserAlreadyRegisteredException("该手机号已注册")
        }
        val newAccount = com.github.bumblebee202111.minusone.server.entity.Account(
            userName = userName,
            phone = phone,
            password = registerRequest.password
        )
        val savedAccount = accountRepository.save(newAccount)
        log.info("New account registered: userName {}, ID {}", savedAccount.userName, savedAccount.id)

        val newProfile = com.github.bumblebee202111.minusone.server.entity.Profile(
            account = savedAccount,
            nickname = registerRequest.nickname
        )
        profileRepository.save(newProfile)
        try {
            val likeList = com.github.bumblebee202111.minusone.server.entity.Playlist(
                name = "喜欢的音乐",
                specialType = com.github.bumblebee202111.minusone.server.entity.Playlist.SpecialType.STAR,
                userId = savedAccount.id,
            )
            playlistRepository.save(likeList)
        } catch (e: Exception) {
            log.error(
                "CRITICAL: Failed to create '喜欢的音乐' playlist for accountId: {}. Error: {}",
                savedAccount.id, e.message, e
            )
            throw RuntimeException("Failed to initialize crucial user data (favorite playlist).", e)
        }

        val tokenString = issueTokenForAccount(savedAccount)

        return Pair(savedAccount, tokenString)
    }

    fun logout(response: HttpServletResponse) {
        response.addCookie(Cookie("MUSIC_U", null))
    }

    @Transactional(readOnly = true)
    fun checkCellphoneExistence(phone: String, countryCode: String?): PhoneAccountInfoDto {
        val account = accountRepository.findByPhone(phone)

        if (account != null) {
            val profile = profileRepository.findById(account.id)
                .orElseThrow {
                    IllegalStateException("Data integrity issue: Account ${account.id} found but has no profile.")
                }
            return PhoneAccountInfoDto.found(account, profile)
        } else {
            return PhoneAccountInfoDto.notFound(phone, countryCode)
        }
    }

}