package com.tsquaredapplications.airvengeance.util

import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.R.NotificationHelper.NotificationHelper
import com.tsquaredapplications.airvengeance.data.DataRepository
import androidx.lifecycle.LifecycleRegistry
import com.tsquaredapplications.airvengeance.MainActivity



@RequiresApi(Build.VERSION_CODES.O)
class PreferenceMonitor : Runnable, LifecycleOwner, Activity() {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return registry
    } //Defining lifeCycle for threading purposes

    override fun run() { // what to run when this class is Run

        /*This class to be treated as a Runnable and run as a background thread
    1.)Pull Firebase Values from FirebaseUtil
    2.)Pull Preference Values from Preference Fragment (or xml?)
    3.)Compare all values, trigger notifications when needed
    */

        //ALL USER PREFERENCE VALUES
        lateinit var minTemp: String
        lateinit var maxTemp: String
        lateinit var minPress: String
        lateinit var maxPress: String
        lateinit var minHumid: String
        lateinit var maxHumid: String

        var prefs = PreferenceManager.getDefaultSharedPreferences(this)
        minTemp = prefs.getString(getString(R.string.minimum_temp),"10")
        maxTemp = prefs.getString(getString(R.string.maximum_temp),"15")
        minPress = prefs.getString(getString(R.string.minimum_press),"15")
        maxPress = prefs.getString(getString(R.string.maximum_press),"20")
        minHumid = prefs.getString(getString(R.string.minimum_humid),"20")
        maxHumid = prefs.getString(getString(R.string.maximum_humid),"40")

        //Firebase Values
        lateinit var fireTemp : String
        lateinit var firePress : String
        lateinit var fireHumid : String

        //Notifications Setup
        var nManager = NotificationHelper(this)
        //nManager.sendNotification("Air Quality Alert","House is too Hot!")

        //Comparing/Sending Notifications

    }
}