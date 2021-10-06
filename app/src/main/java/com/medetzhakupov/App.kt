package com.medetzhakupov

import android.app.Application

class App : Application() {

    private val injector: Injector by lazy { Injector(this) }

    val mainInjector: Injector
        get() = injector
}