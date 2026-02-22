package com.droid.shopping

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ShoppingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // In release, plant a crash-reporting tree here (e.g. Crashlytics).
        // Example: Timber.plant(CrashlyticsTree())
    }
}
