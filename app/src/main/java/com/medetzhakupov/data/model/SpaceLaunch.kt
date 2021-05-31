package com.medetzhakupov.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpaceLaunch(
    val id: String,
    val image: String,
    val mission: Mission? = null,
    val name: String,
    val pad: Pad,
): Parcelable