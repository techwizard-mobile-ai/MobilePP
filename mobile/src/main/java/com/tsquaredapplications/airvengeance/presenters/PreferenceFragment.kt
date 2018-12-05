package com.tsquaredapplications.airvengeance.presenters

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.firebase.database.FirebaseDatabase
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import com.tsquaredapplications.airvengeance.R



class PreferenceFragment : PreferenceFragmentCompat(),
SharedPreferences.OnSharedPreferenceChangeListener
{
    lateinit var minT: String
    lateinit var maxT: String
    lateinit var minP: String
    lateinit var maxP: String
    lateinit var minH: String
    lateinit var maxH: String
    lateinit var zip: String
    lateinit var time: String
    lateinit var prefs: SharedPreferences


    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val minTemp = findPreference(getString(R.string.minimum_temp))
        val maxTemp = findPreference(getString(R.string.maximum_temp))
        val minHumid = findPreference(getString(R.string.minimum_humid))
        val maxHumid = findPreference(getString(R.string.maximum_humid))
        val minPress = findPreference(getString(R.string.minimum_press))
        val maxPress = findPreference(getString(R.string.maximum_press))
        val zipCode = findPreference(getString(R.string.zip_code))
        val timeInterval = findPreference("Time Between Readings")


        prefs = PreferenceManager.getDefaultSharedPreferences(activity)



        minT = prefs.getString(getString(R.string.minimum_temp),"10")
        minTemp.title = "Minimum Temperature : $minT"

        maxT = prefs.getString(getString(R.string.maximum_temp),"15")
        maxTemp.title = "Maximum Temperature : $maxT"

        minP = prefs.getString(getString(R.string.minimum_press),"15")
        minPress.title = "Minimum Pressure : $minP"

        maxP = prefs.getString(getString(R.string.maximum_press),"20")
        maxPress.title = "Maximum Pressure : $maxP"

        minH = prefs.getString(getString(R.string.minimum_humid),"20")
        minHumid.title = "Minimum Humidity : $minH"

        maxH = prefs.getString(getString(R.string.maximum_humid),"40")
        maxHumid.title = "Maximum Humiity : $maxH"

        zip = prefs.getString(getString(R.string.zip_code),"14127")
        zipCode.title = "Zip Code : $zip"

        time = prefs.getString(getString(R.string.time_interval_sec),"10")
        timeInterval.title = "Time Between Readings : $time"
    }


    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        //monitor changes to preferences here
        val timer = getString(R.string.time_interval_sec)
        val minTemp = getString(R.string.minimum_temp)
        val maxTemp = getString(R.string.maximum_temp)
        val minPress = getString(R.string.minimum_press)
        val maxPress = getString(R.string.maximum_press)
        val minHumid = getString(R.string.minimum_humid)
        val maxHumid = getString(R.string.maximum_humid)
        val zip = getString(R.string.zip_code)

        when(key) {
            timer -> {
                FirebaseDatabase.getInstance().reference.child("TIMER").setValue(sharedPreferences.getString(timer, "10"))
                var currentTime = prefs.getString(getString(R.string.time_interval_sec), "10")
                if(currentTime.length==0){currentTime="10"
                    sharedPreferences.edit().putString(timer, currentTime)}
                findPreference(getString(R.string.time_interval_sec)).title = "Time Between Readings : $currentTime"
            }

            zip ->{
                var currentZip = prefs.getString(getString(R.string.zip_code),"14224")
                if(currentZip.length == 0){currentZip = "14224"
                    sharedPreferences.edit().putString(zip,currentZip).apply()}
                findPreference(getString(R.string.zip_code)).title = "Zip Code : $currentZip"

            }

            minTemp -> {
                var currentMinTemp = prefs.getString(getString(R.string.minimum_temp),"10")
                if(currentMinTemp.length == 0){currentMinTemp = "10"
                    sharedPreferences.edit().putString(minTemp, currentMinTemp).apply()}
                findPreference(getString(R.string.minimum_temp)).title = "Minimum Temperature : $currentMinTemp"

            }

            maxTemp-> {
                var currentMaxTemp = prefs.getString(getString(R.string.maximum_temp),"15")
                if(currentMaxTemp.length == 0){currentMaxTemp = "15"
                    sharedPreferences.edit().putString(maxTemp, currentMaxTemp)}
                findPreference(getString(R.string.maximum_temp)).title = "Maximum Temperature : $currentMaxTemp"
            }

            minPress ->{
                var currentMinPress = prefs.getString(getString(R.string.minimum_press),"15")
                if(currentMinPress.length==0){currentMinPress = "15"
                    sharedPreferences.edit().putString(minTemp, currentMinPress)}
                findPreference(getString(R.string.minimum_press)).title = "Minimum Pressure : $currentMinPress"
            }

            maxPress->{
                var currentMaxPress = prefs.getString(getString(R.string.maximum_press),"20")
                if(currentMaxPress.length==0){currentMaxPress="20"
                    sharedPreferences.edit().putString(minTemp, currentMaxPress)}
                findPreference(getString(R.string.maximum_press)).title = "Maximum Pressure : $currentMaxPress"
            }

            minHumid->{
                var currentMinHumid = prefs.getString(getString(R.string.minimum_humid),"20")
                if(currentMinHumid.length==0){currentMinHumid="20"
                    sharedPreferences.edit().putString(minHumid, currentMinHumid)}
                findPreference(getString(R.string.minimum_humid)).title = "Minimum Humidity : $currentMinHumid"
            }

            maxHumid -> {
                var currentMaxHumid = prefs.getString(getString(R.string.maximum_humid),"40")
                if(currentMaxHumid.length==0){currentMaxHumid="40"
                    sharedPreferences.edit().putString(maxHumid, currentMaxHumid)}
                findPreference(getString(R.string.maximum_humid)).title = "Maximum Humidity : $currentMaxHumid"
            }

        }
    }
}
