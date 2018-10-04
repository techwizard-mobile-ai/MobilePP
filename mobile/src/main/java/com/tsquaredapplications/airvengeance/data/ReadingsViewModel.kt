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
    private val prefs = PreferenceManager.getDefaultSharedPreferences(application)
    private val isMetricKey = application.getString(R.string.is_metric_key)

    private val dataRepo: DataRepository

    init {

        isMetric = prefs.getBoolean(isMetricKey, true)


        dataRepo = DataRepository()
    }

    fun getPrefs(){

        isMetric = prefs.getBoolean(PreferenceKeys.IS_METRIC_KEY, true)
    }

    fun getTempReading(tempC: Float) = if(isMetric) tempC.toString() else imperialValue(tempC).toString()

    private fun imperialValue(temp: Float)=  ((temp * 9 / 5.0) + 32)

    fun isMetric(): Boolean {
        getPrefs()
        return isMetric
    }

    fun getDataStream(): MutableLiveData<List<Data>> {
        return dataRepo.getDataStream()
    }


}