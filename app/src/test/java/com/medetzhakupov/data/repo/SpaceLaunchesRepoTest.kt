package com.medetzhakupov.data.repo

import com.medetzhakupov.CoroutinesTestRule
import com.medetzhakupov.data.model.SpaceLaunches
import com.medetzhakupov.data.remote.SpaceLaunchesService
import com.medetzhakupov.dispatchers.MainDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

@ExperimentalCoroutinesApi
class SpaceLaunchesRepoTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val service: SpaceLaunchesService = mock()

    private val repo = SpaceLaunchesRepo(service, MainDispatcherProvider)

    @Test
    fun `should return list of space launches`(): Unit = runBlockingTest {
        val spaceLaunches = mock<SpaceLaunches>()

        service.stub {
            onBlocking { getSpaceLaunches(any()) } doReturn spaceLaunches
        }

        repo.getSpaceLaunches().collect { result ->
            assertThat(result.isSuccess).isTrue
            result.onSuccess { assertThat(it).isEqualTo(spaceLaunches) }
        }
    }

    @Test
    fun `should fail when getting list of space launches`(): Unit = runBlockingTest {
        service.stub {
            onBlocking { getSpaceLaunches(any()) } doAnswer { throw Exception() }
        }

        repo.getSpaceLaunches().collect { result ->
            assertThat(result.isFailure).isTrue
        }
    }

    @Test
    fun `should return searched list of space launches`(): Unit = runBlockingTest {
        val spaceLaunches: SpaceLaunches = mock()
        val search = "SpaceX"

        service.stub {
            onBlocking { searchSpaceLaunches(search) } doReturn spaceLaunches
        }

        repo.searchSpaceLaunches(search).collect { result ->
            assertThat(result.isSuccess).isTrue
            result.onSuccess { assertThat(it).isEqualTo(spaceLaunches) }
        }
    }

    @Test
    fun `should fail when searching space launches`(): Unit = runBlockingTest {
        val search = "SpaceX"

        service.stub {
            onBlocking { searchSpaceLaunches(search) } doAnswer { throw Exception() }
        }

        repo.searchSpaceLaunches(search).collect { result ->
            assertThat(result.isFailure).isTrue
        }
    }
}