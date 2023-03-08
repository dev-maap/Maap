package com.dev.maap.domain.usecase.base

abstract class SuspendUseCase<in PARAMETER, out RESPONSE> {
    protected abstract suspend fun execute(parameter: PARAMETER) : RESPONSE
    suspend operator fun invoke(parameter: PARAMETER) = execute(parameter)
}