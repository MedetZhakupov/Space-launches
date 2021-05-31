package com.medetzhakupov.ui.main

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource
import androidx.viewpager.widget.ViewPager

class RecyclerViewIdlingResources(recyclerView: RecyclerView) : IdlingResource {
    private var idle = true
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    override fun getName(): String = "RecyclerView Idling Resource"

    override fun isIdleNow(): Boolean = idle

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }
}