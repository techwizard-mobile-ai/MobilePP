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


        humidity_tv.text = getString(R.string.humidity_reading, viewModel.humidity.toString())
        pressure_tv.text = getString(R.string.pressure_reading, viewModel.pressure.toString())
        pm2_5_tv.text = getString(R.string.pm25_reading, viewModel.pm25.toString())


    }
}
