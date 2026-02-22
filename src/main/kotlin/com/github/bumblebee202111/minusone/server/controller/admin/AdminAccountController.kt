package com.github.bumblebee202111.minusone.server.controller.admin

import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminAccountCreateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminAccountUpdateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminAccountDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminUserCreateResponseDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.PageResponseDto
import com.github.bumblebee202111.minusone.server.service.admin.AdminAccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/accounts")
@Tag(name = "Admin Account", description = "Admin Account related APIs")
class AdminAccountController(private val accountService: AdminAccountService) {

    @GetMapping
    @Operation(summary = "List accounts", description = "Lists accounts with pagination")
    fun listAccounts(@RequestParam page: Int, @RequestParam size: Int): PageResponseDto<AdminAccountDto> {
        return accountService.listAccounts(page,size)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account", description = "Gets an account by ID")
    fun getAccount(@PathVariable("id") @Positive userId: Long): AdminAccountDto {
        return accountService.getAccount(userId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create account", description = "Creates a new account")
    fun createAccount(@Valid @RequestBody req: AdminAccountCreateRequest): AdminUserCreateResponseDto {
        return accountService.createAccount(req)
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update account", description = "Updates an account by ID")
    fun updateAccount(@Positive @PathVariable userId: Long, @RequestBody @Valid req: AdminAccountUpdateRequest): AdminAccountDto {
        return accountService.updateAccount(userId, req)
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete account", description = "Deletes an account by ID")
    fun deleteAccount(@PathVariable userId: Long) {
        accountService.deleteAccount(userId)
    }
}
