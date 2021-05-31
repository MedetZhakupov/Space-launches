package com.medetzhakupov.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provider interface for coroutine dispatchers.
 */
interface DispatcherProvider {

    /**
     * Main (UI) thread dispatcher.
     */
    val mainDispatcher: CoroutineDispatcher

    /**
     * Dispatcher for performing background tasks.
     */
    val backgroundDispatcher: CoroutineDispatcher
}