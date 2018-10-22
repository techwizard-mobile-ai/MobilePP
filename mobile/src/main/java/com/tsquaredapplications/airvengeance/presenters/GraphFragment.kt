package com.tsquaredapplications.airvengeance.presenters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jjoe64.graphview.series.DataPoint

import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.data.ReadingsViewModel
import com.jjoe64.graphview.series.LineGraphSeries
import com.tsquaredapplications.airvengeance.data.Data
import kotlinx.android.synthetic.main.fragment_graph.*


class GraphFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(activity!!).get(ReadingsViewModel::class.java) }
    var dataType = 0
    lateinit var series: LineGraphSeries<DataPoint>
    lateinit var liveData: MutableLiveData<List<Data>>
    lateinit var observer: Observer<List<Data>>
    val fragment = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer = Observer {
            it.reversed().let { nonNullList ->
                if (graph_view.series.isEmpty()) {
                    series = getLineGraphSeries(dataType, nonNullList)
                    graph_view.addSeries(series)
                } else {
                    series.appendData(DataPoint((nonNullList.size - 1).toDouble(), getNewValue(nonNullList[0])),
                            false, 40)
                }
            }
        }

        liveData = viewModel.getDataStream()
        liveData.observe(this, observer)

        graph_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do Nothing
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                liveData.removeObserver(observer)
                dataType = position
                graph_view.removeAllSeries()
                liveData.observe(fragment, observer)
            }

        }

    }

    private fun getNewValue(data: Data): Double =
            when (dataType) {
                0 -> data.temp?.toDouble()!!
                1 -> data.humidity?.toDouble()!!
                2 -> data.pressure?.toDouble()!!
                3 -> data.pm25?.toDouble()!!
                4 -> data.pm10?.toDouble()!!
                else -> 0.0
            }


    /**
     * For input param type
     *
     * 0 = temp
     * 1 = humidity
     * 2 = pressure
     * 3 = pm25
     * 4 = pm10
     */
    private fun getLineGraphSeries(type: Int, dataList: List<Data>): LineGraphSeries<DataPoint> {
        val dataArray = arrayOfNulls<DataPoint>(dataList.size)

        when (type) {
            0 -> {
                for (i in 0 until dataList.size) {
                    dataArray[i] = DataPoint(i.toDouble(), dataList[i].temp!!.toDouble())
                }
            }
            1 -> {
                for (i in 0 until dataList.size) {
                    dataArray[i] = DataPoint(i.toDouble(), dataList[i].humidity!!.toDouble())
                }
            }
            2 -> {
                for (i in 0 until dataList.size) {
                    dataArray[i] = DataPoint(i.toDouble(), dataList[i].pressure!!.toDouble())
                }
            }
            3 -> {
                for (i in 0 until dataList.size) {
                    dataArray[i] = DataPoint(i.toDouble(), dataList[i].pm25!!.toDouble())
                }
            }
            4 -> {
                for (i in 0 until dataList.size) {
                    dataArray[i] = DataPoint(i.toDouble(), dataList[i].pm10!!.toDouble())
                }
            }
        }
        return LineGraphSeries<DataPoint>(dataArray)

    }

}
