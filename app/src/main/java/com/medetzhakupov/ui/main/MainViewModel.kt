package com.medetzhakupov.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medetzhakupov.data.model.SpaceLaunches
import com.medetzhakupov.data.repo.SpaceLaunchesRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: SpaceLaunchesRepo,
    private val preferences: SharedPreferences,
    ) : ViewModel() {

    private val _spaceLaunchesState = MutableLiveData<SpaceLaunchesState>()
    private val seenLaunches = preferences.getStringSet("space_launch_id", setOf())

    val spaceLaunchesState: LiveData<SpaceLaunchesState> = _spaceLaunchesState

    init {
        loadSpaceLaunches()
    }

    fun search(search: String) {
        _spaceLaunchesState.value = SpaceLaunchesState.Loading
        if (search.isBlank()) {
            loadSpaceLaunches()
            return
        }

        viewModelScope.launch {
            repo.searchSpaceLaunches(search).collect { result ->
                result.onSuccess {
                    _spaceLaunchesState.value = SpaceLaunchesState.Loaded(it, seenLaunches)
                }

                result.onFailure {
                    _spaceLaunchesState.value = SpaceLaunchesState.Failed
                }
            }
        }
    }

    private fun loadSpaceLaunches() {
        _spaceLaunchesState.value = (SpaceLaunchesState.Loading)
        viewModelScope.launch {
            repo.getSpaceLaunches()
                .collect { result ->
                result.onSuccess {
                    _spaceLaunchesState.value =
                        SpaceLaunchesState.Loaded(it, seenLaunches)
                }

                result.onFailure {
                    _spaceLaunchesState.value = SpaceLaunchesState.Failed
                }
            }
        }
    }
}

sealed class SpaceLaunchesState {
    object Loading : SpaceLaunchesState()
    data class Loaded(val spaceLaunches: SpaceLaunches, val seenLaunches: Set<String>?) : SpaceLaunchesState()
    object Failed : SpaceLaunchesState()
}