package com.tsquaredapplications.airvengeance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tsquaredapplications.airvengeance.presenters.HistoryFragment
import com.tsquaredapplications.airvengeance.presenters.OutdoorReadingsFragment
import com.tsquaredapplications.airvengeance.presenters.PreferenceFragment
import com.tsquaredapplications.airvengeance.presenters.ReadingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host)
        setupNavigation()
    }

    private fun setupNavigation() {
        NavigationUI.setupWithNavController(bottom_navigation, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_settings -> {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
        return true
    }
}

