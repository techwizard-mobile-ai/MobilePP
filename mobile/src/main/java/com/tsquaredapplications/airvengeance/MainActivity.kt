package com.tsquaredapplications.airvengeance

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperature_tv.text = getString(R.string.temperature_reading, "24")
        humidity_tv.text = getString(R.string.humidity_reading, "56")
        pressure_tv.text = getString(R.string.pressure_reading, "20")
        pm2_5_tv.text = getString(R.string.pm25_reading, "20")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_settings -> {

            }
        }
        return true
    }
}
