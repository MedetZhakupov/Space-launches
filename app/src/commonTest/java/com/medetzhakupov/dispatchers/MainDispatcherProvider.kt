package com.medetzhakupov.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object MainDispatcherProvider : DispatcherProvider {

    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override val backgroundDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
}
