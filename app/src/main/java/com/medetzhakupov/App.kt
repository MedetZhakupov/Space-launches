package com.medetzhakupov

import android.app.Application

class App : Application() {

    private val injector: Injector by lazy { Injector() }

    val mainInjector: Injector
        get() = injector
}