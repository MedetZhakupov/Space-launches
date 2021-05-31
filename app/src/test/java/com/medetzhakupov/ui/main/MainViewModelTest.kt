package com.medetzhakupov.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.medetzhakupov.CoroutinesTestRule
import com.medetzhakupov.data.model.SpaceLaunches
import com.medetzhakupov.data.remote.SpaceLaunchesService
import com.medetzhakupov.data.repo.SpaceLaunchesRepo
import com.medetzhakupov.dispatchers.MainDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class MainViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val spaceLaunches: SpaceLaunches = mock()
    private val service: SpaceLaunchesService = mock {
        onBlocking { getSpaceLaunches(any()) } doReturn spaceLaunches
    }
    private val repo = spy(SpaceLaunchesRepo(service, MainDispatcherProvider))

    @Test
    fun `should get list of space launches and update ui state`() {
        MainViewModel(repo).apply {
            assertThat(spaceLaunchesState.value).isEqualTo(SpaceLaunchesState.Loaded(spaceLaunches))
        }
    }

    @Test
    fun `should search space launches and update ui state`() {
        val search = "SpaceX"
        val searchResults: SpaceLaunches = mock()

        service.stub {
            onBlocking { searchSpaceLaunches(search) } doReturn searchResults
        }

        MainViewModel(repo).apply {
            search(search)

            assertThat(spaceLaunchesState.value).isEqualTo(SpaceLaunchesState.Loaded(searchResults))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `should load space launches if search text is empty and update ui state`() = runBlockingTest {
        val search = ""

        MainViewModel(repo).apply {
            search(search)

            verify(repo, never()).searchSpaceLaunches(any())
            assertThat(spaceLaunchesState.value).isEqualTo(SpaceLaunchesState.Loaded(spaceLaunches))
        }
    }
}
