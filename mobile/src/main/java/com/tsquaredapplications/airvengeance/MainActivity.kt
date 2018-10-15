package com.tsquaredapplications.airvengeance

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.FirebaseApp
import com.tsquaredapplications.airvengeance.presenters.PreferenceFragment
import com.tsquaredapplications.airvengeance.presenters.ReadingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        // load readings fragment by default
        swapFragments(ReadingsFragment(), true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_settings -> {
                if(supportFragmentManager.findFragmentById(R.id.content_frame) !is PreferenceFragment)
                swapFragments(PreferenceFragment(), true)
            }
        }
        return true
    }

    fun swapFragments(fragment: Fragment, addToBackStack: Boolean) {
        when (addToBackStack) {
            true -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit()
            }
            false -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .disallowAddToBackStack()
                        .commit()
            }
        }
    }
}
