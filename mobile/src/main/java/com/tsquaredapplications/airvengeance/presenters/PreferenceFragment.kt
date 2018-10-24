package com.tsquaredapplications.airvengeance.presenters

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.google.firebase.database.FirebaseDatabase
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import com.tsquaredapplications.airvengeance.R


class PreferenceFragment : PreferenceFragmentCompat(),
SharedPreferences.OnSharedPreferenceChangeListener
{
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
        if (key == timer) {//if the timer changes
            //send to firebase
            6
            val dbRef = FirebaseDatabase.getInstance().reference
                    .child("TIMER")
                    .setValue(sharedPreferences.getString(timer, "10"))

        }
    }


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


        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)


        val mT = prefs.getString(getString(R.string.minimum_temp),"10")
        minTemp.setTitle("Minimum Temperature : $mT" )

        val MT = prefs.getString(getString(R.string.maximum_temp),"15")
        maxTemp.setTitle("Maximum Temperature : $MT" )

        val mP = prefs.getString(getString(R.string.minimum_press),"15")
        minPress.setTitle("Minimum Pressure : $mP" )

        val MP = prefs.getString(getString(R.string.maximum_press),"20")
        maxPress.setTitle("Maximum Pressure : $MP" )

        val mH = prefs.getString(getString(R.string.minimum_humid),"20")
        minHumid.setTitle("Minimum Humidity : $mH" )

        val MH = prefs.getString(getString(R.string.maximum_humid),"40")
        maxHumid.setTitle("Maximum Humiity : $MH" )

        val zip = prefs.getString(getString(R.string.zip_code),"14127")
        zipCode.setTitle("Zip Code : $zip")

        val time = prefs.getString(getString(R.string.time_interval_sec),"10")
        timeInterval.setTitle("Time Between Readings : $time")
    }



}
