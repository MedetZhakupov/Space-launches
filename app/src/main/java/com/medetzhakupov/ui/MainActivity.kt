package com.medetzhakupov.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.medetzhakupov.App
import com.medetzhakupov.Injector
import com.medetzhakupov.R
import com.medetzhakupov.ui.details.SpaceLaunchDetailsFragment
import com.medetzhakupov.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private val mainInjector: Injector by lazy { (application as App).mainInjector }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = fragmentFactory(::createFragment)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            showFragment(createFragment<MainFragment>())
        }
    }

    private fun showFragment(fragmentToShow: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.container, fragmentToShow, fragmentToShow::class.java.canonicalName)
            addToBackStack(null)
        }
    }

    private fun createFragment(fragmentClass: Class<out Fragment>): Fragment =
        when (fragmentClass) {
            MainFragment::class.java -> MainFragment(mainInjector.provideSpaceLaunchesRepo(), mainInjector.provideSeenSpaceLaunchesRepo()) {
                showFragment(createFragment<SpaceLaunchDetailsFragment>().withArguments(it))
            }
            SpaceLaunchDetailsFragment::class.java -> SpaceLaunchDetailsFragment(mainInjector.provideSeenSpaceLaunchesRepo())
            else -> throw IllegalArgumentException("Unknown fragment $fragmentClass")
        }
}