package com.tsquaredapplications.airvengeance.presenters


import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.tsquaredapplications.airvengeance.R

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}
