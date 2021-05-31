package com.medetzhakupov.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val id: Int,
    val map_image: String,
    val name: String,
): Parcelable