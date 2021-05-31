package com.medetzhakupov.data.remote

import com.medetzhakupov.data.model.SpaceLaunches
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceLaunchesService {

    @GET("launch/upcoming")
    suspend fun getSpaceLaunches(@Query("limit") limit: Int) : SpaceLaunches

    @GET("launch/upcoming")
    suspend fun searchSpaceLaunches(@Query("search") search: String) : SpaceLaunches

}