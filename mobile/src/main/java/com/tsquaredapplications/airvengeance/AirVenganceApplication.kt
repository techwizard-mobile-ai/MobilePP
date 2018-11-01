package com.tsquaredapplications.airvengeance

import android.app.Application
import android.util.Log
import com.squareup.leakcanary.LeakCanary

class AirVenganceApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("AirVenganceApplication", "onCreate")
        if(LeakCanary.isInAnalyzerProcess(this)) return
        else LeakCanary.install(this)
    }
}