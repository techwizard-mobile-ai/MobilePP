package com.tsquaredapplications.airvengeance

import android.app.Application
import android.util.Log

class AirVenganceApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("AirVenganceApplication", "onCreate")

    }
}