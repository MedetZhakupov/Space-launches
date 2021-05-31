package com.medetzhakupov.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mission(
    val id: Int,
    val description: String,
): Parcelable