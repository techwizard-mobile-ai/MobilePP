package com.tsquaredapplications.airvengeance.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private fun getPrefs(){
        isMetric = prefs.getBoolean(PreferenceKeys.IS_METRIC_KEY, true)
    }

    fun getTempReading(tempC: Float): Float = if(isMetric) tempC else imperialValue(tempC)

    private fun imperialValue(temp: Float)=  ((temp * 9 / 5.0) + 32).toFloat()

    fun isMetric(): Boolean {
        getPrefs()
        return isMetric
    }

    fun getDataStream(): MutableLiveData<List<Data>> {
        return dataRepo.getDataStream()
    }


}