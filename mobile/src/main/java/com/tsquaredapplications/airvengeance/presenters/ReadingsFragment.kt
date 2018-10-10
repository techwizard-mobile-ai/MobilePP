package com.tsquaredapplications.airvengeance.presenters


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.data.ReadingsViewModel
import kotlinx.android.synthetic.main.fragment_readings.*


class ReadingsFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(ReadingsViewModel::class.java) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_readings, container, false)
    }


    override fun onResume() {
        super.onResume()
        val tempUnitLabel = if(viewModel.isMetric()) 'C' else 'F'

        viewModel.getDataStream().observe(this, Observer {
            it?.let { nonNullList ->
                val recentData = nonNullList[nonNullList.size - 1]

                recentData.temp?.let { temp->
                    temperature_gauge.setSpeed(temp)
                    temperature_gauge.setSpeed(temp)
                }

                recentData.humidity?.let { humidity ->
                    humidity_gauge.setSpeed(humidity)
                }

                recentData.pressure?.let { pressure ->
                    pressure_gauge.setSpeed(pressure)
                }

                recentData.pm25?.let { pm25 ->
                    pm25_gauge.setSpeed(pm25.toFloat())
                }

                recentData.pm10?.let { pm10 ->
                    pm10_gauge.setSpeed(pm10.toFloat())
                }
            }
        })
    }
}
