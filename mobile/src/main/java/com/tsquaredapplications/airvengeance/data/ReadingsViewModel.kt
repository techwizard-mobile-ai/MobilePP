package com.tsquaredapplications.airvengeance.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tsquaredapplications.airvengeance.R
import java.util.prefs.Preferences

class ReadingsViewModel(application: Application): AndroidViewModel(application) {
    private var isMetric: Boolean
    private var temp: Double
    var humidity: Double
    var pressure: Double
    var pm25: Double
    private val prefs = PreferenceManager.getDefaultSharedPreferences(application)
    private val isMetricKey = application.getString(R.string.is_metric_key)

    private val dataRepo: DataRepository

    init {

        isMetric = prefs.getBoolean(isMetricKey, true)
        temp = 24.7
        humidity = 30.4
        pressure = 20.2
        pm25 = 20.1

        dataRepo = DataRepository()
    }

    fun getPrefs(){

        isMetric = prefs.getBoolean(PreferenceKeys.IS_METRIC_KEY, true)
    }

    fun getTempReading() = if(isMetric) temp.toString() else imperialValue(temp).toString()

    private fun imperialValue(temp: Double)=  ((temp * 9 / 5.0) + 32)

    fun isMetric(): Boolean {
        getPrefs()
        return isMetric
    }

    fun getDataStream(): MutableLiveData<List<Data>> {
        return dataRepo.getDataStream()
    }


}