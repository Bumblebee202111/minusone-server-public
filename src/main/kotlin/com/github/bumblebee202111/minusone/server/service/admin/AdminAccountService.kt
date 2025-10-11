package com.github.bumblebee202111.minusone.server.service.admin

import com.github.bumblebee202111.minusone.server.repository.AccountRepository
import com.github.bumblebee202111.minusone.server.repository.ProfileRepository
import com.github.bumblebee202111.minusone.server.dto.admin.request.*
import com.github.bumblebee202111.minusone.server.dto.admin.response.*
import com.github.bumblebee202111.minusone.server.exception.admin.DataConflictException
import com.github.bumblebee202111.minusone.server.exception.admin.ResourceNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminAccountService(
    private val accountRepository: AccountRepository,
    private val profileRepository: ProfileRepository
) {

    @Transactional(readOnly = true)
    fun listAccounts(page: Int, size: Int): PageResponseDto<AdminAccountDto> {
        val pageable = PageRequest.of(page, size, Sort.by("id").ascending())
        val dtoPage= accountRepository.findAll(pageable).map { it.toAdminAccountDto() }
        return PageResponseDto.from(dtoPage)
    }

    @Transactional(readOnly = true)
    fun getAccount(userId: Long): AdminAccountDto {
        return accountRepository.findById(userId).orElseThrow { ResourceNotFoundException("Account not found with ID: $userId") } .toAdminAccountDto()
    }

    @Transactional
    fun createAccount(request: AdminAccountCreateRequest): AdminUserCreateResponseDto {
        val userName = "1_${request.phone}"
        if (accountRepository.existsByUserName(userName)) {
            throw DataConflictException("Account with username '$userName' already exists.")
        }
        val newAccount = request.toAccountEntity()
        val savedAccount = accountRepository.save(newAccount)
        val profileToCreate = request.toProfileEntity(savedAccount)
        val savedProfile = profileRepository.save(profileToCreate)
        return AdminUserCreateResponseDto(
            account = savedAccount.toAdminAccountDto(),
            profile = savedProfile.toAdminProfileDto()
        )
    }

    @Transactional
    fun updateAccount(userId: Long, req: AdminAccountUpdateRequest): AdminAccountDto {
        val account = accountRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("Account not found with ID: $userId") }
        return accountRepository.save(req.updateEntity(account)).toAdminAccountDto()
    }

    @Transactional
    fun deleteAccount(userId: Long) {
        if (!accountRepository.existsById(userId)) {
            return
        }
        profileRepository.deleteById(userId)
        accountRepository.deleteById(userId)
    }
}