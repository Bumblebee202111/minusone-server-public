package com.github.bumblebee202111.minusone.server.controller.admin

import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminAccountCreateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminAccountUpdateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminAccountDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminUserCreateResponseDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.PageResponseDto
import com.github.bumblebee202111.minusone.server.service.admin.AdminAccountService
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/accounts")
class AdminAccountController(private val accountService: AdminAccountService) {

    @GetMapping
    fun listAccounts(@RequestParam page: Int, @RequestParam size: Int): PageResponseDto<AdminAccountDto> {
        return accountService.listAccounts(page,size)
    }

    @GetMapping("/{id}")
    fun getAccount(@PathVariable("id") @Positive userId: Long): AdminAccountDto {
        return accountService.getAccount(userId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@Valid @RequestBody req: AdminAccountCreateRequest): AdminUserCreateResponseDto {
        return accountService.createAccount(req)
    }

    @PutMapping("/{userId}")
    fun updateAccount(@Positive @PathVariable userId: Long, @RequestBody @Valid req: AdminAccountUpdateRequest): AdminAccountDto {
        return accountService.updateAccount(userId, req)
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(@PathVariable userId: Long) {
        accountService.deleteAccount(userId)
    }
}