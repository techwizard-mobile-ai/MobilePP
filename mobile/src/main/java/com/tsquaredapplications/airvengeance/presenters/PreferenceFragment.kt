package com.tsquaredapplications.airvengeance.presenters


import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceManager
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.R.string.minimum_temp
import com.tsquaredapplications.airvengeance.R.string.preferences


class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.preferences)

        val minTemp = findPreference(getString(R.string.minimum_temp))
        val maxTemp = findPreference(getString(R.string.maximum_temp))
        val minHumid = findPreference(getString(R.string.minimum_humid))
        val maxHumid = findPreference(getString(R.string.maximum_humid))
        val minPress = findPreference(getString(R.string.minimum_press))
        val maxPress = findPreference(getString(R.string.maximum_press))
        val zipCode = findPreference(getString(R.string.zip_code))

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

    }






}
