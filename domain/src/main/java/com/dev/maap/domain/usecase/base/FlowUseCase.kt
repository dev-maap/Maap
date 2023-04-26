package com.dev.maap.domain.usecase.base

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<in PARAMETER, out RESPONSE> {
    protected abstract fun execute(parameter: PARAMETER) : Flow<RESPONSE>

    operator fun invoke(parameter: PARAMETER) : Flow<RESPONSE> = execute(parameter)
}