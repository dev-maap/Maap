package com.dev.maap.domain.usecase.base

abstract class BaseUseCase<in PARAMETER, out RESPONSE> {
    protected abstract fun execute(parameter: PARAMETER) : RESPONSE
    operator fun invoke(parameter: PARAMETER) = execute(parameter)
}