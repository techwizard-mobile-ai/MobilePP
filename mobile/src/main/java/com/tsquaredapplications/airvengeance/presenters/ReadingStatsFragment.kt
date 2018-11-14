package com.tsquaredapplications.airvengeance.presenters

import `in`.unicodelabs.kdgaugeview.KdGaugeView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders

import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.adapters.SparkLineAdapter
import com.tsquaredapplications.airvengeance.data.Data
import com.tsquaredapplications.airvengeance.data.ReadingsViewModel
import kotlinx.android.synthetic.main.fragment_reading_stats.*
import java.util.*


class ReadingStatsFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(ReadingsViewModel::class.java) }
    private val millisInDay: Long = 86400000
    var liveData: MutableLiveData<List<Data>> = MutableLiveData()
    var timeInterval = 0
    private var readingType = 0
    lateinit var gaugeView: KdGaugeView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        day_chip.isChecked = true

        liveData = viewModel.getDataStream()
        readingType = ReadingStatsFragmentArgs.fromBundle(arguments).readingType

        when (readingType) {
            0 -> {
                metric_label.text = getString(R.string.temperature)
                when (viewModel.isMetric()) {
                    true -> {
                        gaugeView = celsius_temperature_gauge
                    }
                    else -> {
                        gaugeView = fahrenheit_temperature_gauge
                    }
                }
                observe(timeInterval)
            }
            1 -> {
                metric_label.text = getString(R.string.humidity)
                gaugeView = humidity_gauge
                observe(timeInterval)
            }
            2 -> {
                metric_label.text = getString(R.string.pressure)
                gaugeView = pressure_gauge
                observe(timeInterval)
            }
            3 -> {
                metric_label.text = getString(R.string.pm25)
                gaugeView = pm25_gauge
                observe(timeInterval)
            }
            4 -> {
                metric_label.text = getString(R.string.pm10)
                gaugeView = pm10_gauge
                observe(timeInterval)
            }
        }

        gaugeView.visibility = View.VISIBLE
    }

    private fun observe(timeInterval: Int) {
        liveData.removeObservers(this)
        liveData.observe(this, androidx.lifecycle.Observer {
            if (!it.isEmpty()) {
                val prunedList = pruneData(it, timeInterval)
                val currentVal = prunedList[prunedList.size - 1]
                if (readingType == 0) {
                    gaugeView.setSpeed(viewModel.getTempReading(currentVal))
                } else {
                    gaugeView.setSpeed(currentVal)
                }
                spark_view.adapter = SparkLineAdapter(prunedList)
            }
        })
    }

    /**
     * intervalType represents the chosen time interval:
     *
     * 0 - day
     * 1 - week
     * 2 - month
     */
    private fun pruneData(dataList: List<Data>, intervalType: Int): List<Float> {
        val today = Calendar.getInstance()
        val timeCutoff = when (intervalType) {
            0 -> {
                // day
                today.timeInMillis - millisInDay
            }
            1 -> {
                // week
                today.timeInMillis - (7 * millisInDay)
            }
            else -> {
                // month
                today.timeInMillis - (30 * millisInDay)
            }
        }

        val cutoffDate = Date(timeCutoff)
        val cutoffCal = Calendar.getInstance()
        cutoffCal.time = cutoffDate
        Log.i("ReadingStatsFragment", "pruneData: Cutoff Date ${cutoffDate.time}")

        val t = Date(today.timeInMillis)
        Log.i("ReadingStatsFragment", "pruneData:  Today ${t.time}")
        val output = arrayListOf<Float>()
        for (entry in dataList) {
            if (entry.timestamp >= cutoffDate.time) {
                when (readingType) {
                    0 -> {
                        output.add(entry.temp!!.toFloat())
                    }
                    1 -> {
                        output.add(entry.humidity!!.toFloat())
                    }
                    2 -> {
                        output.add(entry.pressure!!.toFloat())
                    }
                    3 -> {
                        output.add(entry.pm25!!.toFloat())
                    }
                    else -> {
                        output.add(entry.pm10!!.toFloat())
                    }
                }
            }


        }
        return output
    }


}
