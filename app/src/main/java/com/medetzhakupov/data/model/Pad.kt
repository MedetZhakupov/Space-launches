package com.medetzhakupov.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pad(
    val id: Int,
    val latitude: String,
    val location: Location,
    val longitude: String,
    val map_image: String,
    val map_url: String,
    val name: String
): Parcelable