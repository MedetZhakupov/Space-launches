package com.medetzhakupov

import com.medetzhakupov.data.remote.SpaceLaunchesClient
import com.medetzhakupov.data.repo.SpaceLaunchesRepo
import kotlinx.coroutines.Dispatchers

class Injector {

    private val spaceLaunchesRepo: SpaceLaunchesRepo by lazy {
        SpaceLaunchesRepo(SpaceLaunchesClient().spaceLaunchesService)
    }

    fun provideSpaceLaunchesRepo() = spaceLaunchesRepo
}