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
    var timeInterval = 2
    private var readingType = 0
    lateinit var gaugeView: KdGaugeView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        month_chip.isChecked = true

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
                observe()
            }
            1 -> {
                metric_label.text = getString(R.string.humidity)
                gaugeView = humidity_gauge
                observe()
            }
            2 -> {
                metric_label.text = getString(R.string.pressure)
                gaugeView = pressure_gauge
                observe()
            }
            3 -> {
                metric_label.text = getString(R.string.pm25)
                gaugeView = pm25_gauge
                observe()
            }
            4 -> {
                metric_label.text = getString(R.string.pm10)
                gaugeView = pm10_gauge
                observe()
            }
        }

        gaugeView.visibility = View.VISIBLE


        // Chip Group listener
        time_interval_chip_group.setOnCheckedChangeListener { _, i ->
            timeInterval = i
            if (i == R.id.day_chip)
                timeInterval = 0
            else if (i == R.id.week_chip)
                timeInterval = 1
            else
                timeInterval = 2

            observe()
        }

    }

    private fun observe() {
        liveData.removeObservers(this)
        liveData.observe(this, androidx.lifecycle.Observer {
            if (!it.isEmpty()) {
                val prunedList = pruneData(it, timeInterval)
                if (prunedList.isNotEmpty()) {
                    val currentVal = prunedList[prunedList.size - 1]
                    if (readingType == 0) {
                        gaugeView.setSpeed(viewModel.getTempReading(currentVal))
                    } else {
                        gaugeView.setSpeed(currentVal)
                    }
                    spark_view.adapter = SparkLineAdapter(prunedList)
                }
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

                val begOfDay = Calendar.getInstance()

                begOfDay.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH),
                        0, 0, 0)

                begOfDay.timeInMillis


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

        // Set padding if selection is day
        if (intervalType == 0) {
            val endOfDay = Calendar.getInstance()

            endOfDay.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH),
                    23, 60, 60)

            val remainingTimeInDay = endOfDay.timeInMillis - today.timeInMillis
            val timeGoneByInDay = millisInDay - remainingTimeInDay
            val daySoFarRation = timeGoneByInDay / millisInDay.toFloat()
            val paddingRight = spark_view.width - Math.round(spark_view.width * daySoFarRation)
            spark_view.setPadding(spark_view.paddingLeft, spark_view.paddingTop,
                    paddingRight, spark_view.paddingBottom)


        } else {
            spark_view.setPadding(0, 0, 0, 0)
        }
        return output
    }


}
