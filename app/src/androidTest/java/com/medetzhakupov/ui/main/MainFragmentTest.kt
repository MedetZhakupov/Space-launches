package com.medetzhakupov.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.IdlingRegistry
import com.medetzhakupov.R
import com.medetzhakupov.SimpleIdlingResource
import com.medetzhakupov.data.model.Location
import com.medetzhakupov.data.model.Pad
import com.medetzhakupov.data.model.SpaceLaunch
import com.medetzhakupov.data.model.SpaceLaunches
import com.medetzhakupov.data.remote.SpaceLaunchesService
import com.medetzhakupov.data.repo.SpaceLaunchesRepo
import com.medetzhakupov.dispatchers.MainDispatcherProvider
import com.medetzhakupov.robot.mainFragment
import com.medetzhakupov.ui.fragmentFactory
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class MainFragmentTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val spaceLaunchesService: SpaceLaunchesService = mock()
    private val spaceLaunchesRepo = SpaceLaunchesRepo(spaceLaunchesService, MainDispatcherProvider)
    private val onSpaceLaunchSelected: ((SpaceLaunch) -> Unit) = mock()

    @Test
    fun shouldShowSpaceLaunches() {
        spaceLaunchesService.stub {
            onBlocking { getSpaceLaunches(any()) } doReturn testSpaceLaunches
        }

        showMainFragment()

        mainFragment {
            checkSpaceLaunchesAreDisplayed()
            clickOnFirstSpaceLaunch()
            verify(onSpaceLaunchSelected).invoke(testSpaceLaunches.results.first())
        }
    }

    @Test
    fun shouldSearchSpaceLaunches() {
        var idlingResource: SimpleIdlingResource? = null
        val search = "Blue Orig"
        spaceLaunchesService.stub {
            onBlocking { getSpaceLaunches(any()) } doReturn SpaceLaunches(results = emptyList())
            onBlocking { searchSpaceLaunches(search) } doReturn searchedSpaceLaunches
        }

        showMainFragment().onFragment {
            idlingResource = it.getIdlingResource()
            IdlingRegistry.getInstance().register(idlingResource)
        }

        mainFragment {
            typeOnSearch(search)
            idlingResource?.setIdleState(false)
            checkSearchedItemIsDisplayed("Blue Origin")
        }

        idlingResource?.let { IdlingRegistry.getInstance().unregister(it) }
    }


    private fun showMainFragment() = launchFragmentInContainer<MainFragment>(
        themeResId = R.style.Theme_RocketLaunch,
        factory = fragmentFactory {
            MainFragment(
                spaceLaunchesRepo,
                onSpaceLaunchSelected
            )
        })

    private val testSpaceLaunches = SpaceLaunches(
        listOf(
            SpaceLaunch(
                id = "1",
                image = "image_url",
                name = "SpaceX",
                pad = Pad(
                    id = 1,
                    latitude = "0",
                    longitude = "0",
                    location = Location(
                        id = 1,
                        map_image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/soyuz25202.1b_image_20210520085921.jpeg",
                        "US"
                    ),
                    map_image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/soyuz25202.1b_image_20210520085921.jpeg",
                    map_url = "map_url",
                    name = "name"
                )
            ),
            SpaceLaunch(
                id = "2",
                image = "image_url",
                name = "SpaceXX",
                pad = Pad(
                    id = 1,
                    latitude = "0",
                    longitude = "0",
                    location = Location(
                        id = 1,
                        map_image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/soyuz25202.1b_image_20210520085921.jpeg",
                        "China"
                    ),
                    map_image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/soyuz25202.1b_image_20210520085921.jpeg",
                    map_url = "map_url",
                    name = "name"
                )
            )
        )
    )

    private val searchedSpaceLaunches = SpaceLaunches(
        results = listOf(
            SpaceLaunch(
                id = "1",
                image = "image_url",
                name = "Blue Origin",
                pad = Pad(
                    id = 1,
                    latitude = "0",
                    longitude = "0",
                    location = Location(
                        id = 1,
                        map_image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/soyuz25202.1b_image_20210520085921.jpeg",
                        "US"
                    ),
                    map_image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/soyuz25202.1b_image_20210520085921.jpeg",
                    map_url = "map_url",
                    name = "Blue Origin"
                )
            )
        )
    )
}