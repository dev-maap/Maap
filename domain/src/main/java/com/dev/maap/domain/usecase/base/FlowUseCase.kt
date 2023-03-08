package com.dev.maap.domain.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in PARAMETER, out RESPONSE>(
    private val dispatcher: CoroutineDispatcher
) {
    protected abstract fun execute(parameter: PARAMETER) : RESPONSE

    operator fun invoke(parameter: PARAMETER) : Flow<RESPONSE> = flow {
        emit(execute(parameter))
    }.flowOn(dispatcher)
}