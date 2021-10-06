package com.medetzhakupov

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.medetzhakupov.data.remote.SpaceLaunchesClient
import com.medetzhakupov.data.repo.SpaceLaunchesRepo
import kotlinx.coroutines.Dispatchers

class Injector {

    private val spaceLaunchesRepo: SpaceLaunchesRepo by lazy {
        SpaceLaunchesRepo(SpaceLaunchesClient().spaceLaunchesService)
    }

    fun provideSpaceLaunchesRepo() = spaceLaunchesRepo

    fun provideSharedPreferences(context: Context) = context.getSharedPreferences("STORE_SEEN_SPACE_LAUNCHES", MODE_PRIVATE)
}