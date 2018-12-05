package com.tsquaredapplications.airvengeance.presenters

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils.split
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.data.ReadingsViewModel
import kotlinx.android.synthetic.main.fragment_readings.*
import java.net.URL
import java.util.ArrayList


class OutdoorReadingsFragment : Fragment() {
    lateinit var prefs: SharedPreferences
    val viewModel by lazy { ViewModelProviders.of(this).get(ReadingsViewModel::class.java) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_readings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup temp gauge for preferred metric
        when (viewModel.isMetric()) {
            true -> {
                fahrenheit_temperature_gauge.visibility = View.INVISIBLE
                celsius_temperature_gauge.visibility = View.VISIBLE
            }
            false -> {
                celsius_temperature_gauge.visibility = View.INVISIBLE
                fahrenheit_temperature_gauge.visibility = View.VISIBLE
            }
        }

        val task = weatherTask()
        task.execute()
        val task2 = weatherTask2()
        task2.execute()
    }

    fun updateUI(data : ArrayList<String>){
        when(viewModel.isMetric()){
            true -> {
                val cTemp = (data[0].toDouble() - 32) * 5 / 9
                celsius_temperature_gauge.setSpeed(cTemp.toFloat())
            }
            false -> {
                fahrenheit_temperature_gauge.setSpeed(data[0].toFloat())
            }
        }
        val pressure = (data[1].toFloat() /  33.863886666667).toFloat()
        pressure_gauge.setSpeed(pressure)
        humidity_gauge.setSpeed(data[2].toFloat())
    }

    fun updateUI2(data : ArrayList<String>){
        pm10_gauge.setSpeed(data[0].toFloat())
        pm25_gauge.setSpeed(data[1].toFloat())
    }

    inner class weatherTask:AsyncTask<Int,Int,ArrayList<String>>(){
        override fun doInBackground(vararg params: Int?): ArrayList<String> {
            val tph = ArrayList<String>()
            val apiKey = "f8c7615401fe9cbca15ce07b8241bd39"

            val testZipCode = "" + prefs.getString("Zip Code", "14224") + ",us"
            val urlString = "https://api.openweathermap.org/data/2.5/weather?q=$testZipCode&units=imperial&appid=$apiKey"
            val resultStr = URL(urlString).readText()
            tph.add(0, resultStr.split("temp\":".toRegex(), 2).toTypedArray()[1].split(",".toRegex(), 2).toTypedArray()[0])
            tph.add(1, resultStr.split("pressure\":".toRegex(), 2).toTypedArray()[1].split(",".toRegex(), 2).toTypedArray()[0])
            tph.add(2, resultStr.split("humidity\":".toRegex(), 2).toTypedArray()[1].split(",".toRegex(), 2).toTypedArray()[0])

            return tph
        }

        override fun onPostExecute(result: ArrayList<String>?) {
            super.onPostExecute(result)
            result?.let {
                updateUI(it)
            }
        }
    }

    inner class weatherTask2:AsyncTask<Int,Int,ArrayList<String>>(){
        override fun doInBackground(vararg params: Int?): ArrayList<String> {
            val pmArray = ArrayList<String>()
            val apiKey = "41f1e1a83522458e97675762a35bc898"
            val urlString = "https://api.breezometer.com/baqi/?lat=40.7324296&lon=-73.9977264&key=$apiKey"
            val resultStr = URL(urlString).readText()
            pmArray.add(0, resultStr.split("pm10\":".toRegex(), 2).toTypedArray()[1].split("concentration\": ".toRegex(),2).toTypedArray()[1].split("\\}".toRegex(),2).toTypedArray()[0])
            pmArray.add(1, resultStr.split("pm25\":".toRegex(), 2).toTypedArray()[1].split("concentration\": ".toRegex(), 2).toTypedArray()[1].split("\\}".toRegex(), 2).toTypedArray()[0])
            return pmArray
        }

        override fun onPostExecute(result: ArrayList<String>?) {
            super.onPostExecute(result)
            result?.let {
                updateUI2(it)
            }
        }
    }
}
