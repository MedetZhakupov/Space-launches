package com.medetzhakupov

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import java.util.concurrent.atomic.AtomicBoolean


class SimpleIdlingResource : IdlingResource {

    @Volatile
    private var mCallback: ResourceCallback? = null

    // Idleness is controlled with this boolean.
    private val mIsIdleNow: AtomicBoolean = AtomicBoolean(true)

    override fun getName(): String = this.javaClass.name

    override fun isIdleNow(): Boolean = mIsIdleNow.get()

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        mCallback = callback
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the [ResourceCallback].
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    fun setIdleState(isIdleNow: Boolean) {
        mIsIdleNow.set(isIdleNow)
        if (isIdleNow && mCallback != null) {
            mCallback?.onTransitionToIdle()
        }
    }
}