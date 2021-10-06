package com.medetzhakupov.data.repo

import android.content.SharedPreferences
import androidx.core.content.edit

const val SEEN_SPACE_LAUNCHES = "SEEN_SPACE_LAUNCHES"

class SeenSpaceLaunchesRepo(private val preferences: SharedPreferences) {

    fun markSpaceLaunchAsSeen(spaceLaunchId: String) {
        preferences.getStringSet(SEEN_SPACE_LAUNCHES, mutableSetOf())?.also { seenSpaceLaunches ->
            seenSpaceLaunches += spaceLaunchId
            preferences.edit {
                putStringSet(SEEN_SPACE_LAUNCHES, seenSpaceLaunches)
            }
        }
    }

    fun isSpaceLaunchSeen(spaceLaunchId: String) = preferences.getStringSet(SEEN_SPACE_LAUNCHES, mutableSetOf())?.contains(spaceLaunchId)
}