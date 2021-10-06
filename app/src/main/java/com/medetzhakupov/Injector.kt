package com.medetzhakupov

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.medetzhakupov.data.remote.SpaceLaunchesClient
import com.medetzhakupov.data.repo.SeenSpaceLaunchesRepo
import com.medetzhakupov.data.repo.SpaceLaunchesRepo

class Injector(context: Context) {

    private val spaceLaunchesRepo: SpaceLaunchesRepo by lazy {
        SpaceLaunchesRepo(SpaceLaunchesClient().spaceLaunchesService)
    }

    private val seenSpaceLaunchesRepo: SeenSpaceLaunchesRepo by lazy {
        SeenSpaceLaunchesRepo(context.getSharedPreferences("SPACE_LAUNCHES", MODE_PRIVATE))
    }

    fun provideSpaceLaunchesRepo() = spaceLaunchesRepo

    fun provideSeenSpaceLaunchesRepo() = seenSpaceLaunchesRepo
}