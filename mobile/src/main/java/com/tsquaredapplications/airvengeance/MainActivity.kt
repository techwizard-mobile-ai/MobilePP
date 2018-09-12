package com.tsquaredapplications.airvengeance

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperature_tv.text = getString(R.string.temperature_reading, "24")
        humidity_tv.text = getString(R.string.humidity_reading, "56")
    }
}
