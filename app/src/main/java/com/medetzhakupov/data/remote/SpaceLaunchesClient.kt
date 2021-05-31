package com.medetzhakupov.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SpaceLaunchesClient {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://lldev.thespacedevs.com/2.2.0/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val spaceLaunchesService : SpaceLaunchesService by lazy {
        retrofit.create(SpaceLaunchesService::class.java)
    }
}
