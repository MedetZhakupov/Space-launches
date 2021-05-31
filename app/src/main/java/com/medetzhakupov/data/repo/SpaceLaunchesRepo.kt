package com.medetzhakupov.data.repo

import com.medetzhakupov.data.model.SpaceLaunches
import com.medetzhakupov.data.remote.SpaceLaunchesService
import com.medetzhakupov.dispatchers.DefaultDispatcherProvider
import com.medetzhakupov.dispatchers.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SpaceLaunchesRepo(
    private val spaceLaunchesService: SpaceLaunchesService,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) {

    fun getSpaceLaunches(limit: Int = 10): Flow<Result<SpaceLaunches>> {
        return flow {
            val spaceLaunches = spaceLaunchesService.getSpaceLaunches(limit)
            emit(Result.success(spaceLaunches))
        }.catch {
            emit(Result.failure(it))
        }.flowOn(dispatcherProvider.mainDispatcher)
    }

    suspend fun searchSpaceLaunches(search: String): Flow<Result<SpaceLaunches>> {
        return flow {
            val spaceLaunches = spaceLaunchesService.searchSpaceLaunches(search)
            emit(Result.success(spaceLaunches))
        }.catch {
            emit(Result.failure(it))
        }.flowOn(dispatcherProvider.mainDispatcher)
    }
}
