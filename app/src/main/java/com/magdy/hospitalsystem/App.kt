package com.magdy.hospitalsystem

import android.app.Application
import com.magdy.hospitalsystem.utils.MySharedPreferences
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App  : Application() {

    override fun onCreate() {
        super.onCreate()

        MySharedPreferences.init(this)
    }
}