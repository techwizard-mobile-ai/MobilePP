package com.tsquaredapplications.airvengeance.presenters


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.adapters.ReadingsListAdapter
import com.tsquaredapplications.airvengeance.data.Data
import com.tsquaredapplications.airvengeance.data.ReadingsViewModel
import kotlinx.android.synthetic.main.fragment_history.*


class HistoryFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(activity!!).get(ReadingsViewModel::class.java) }

    var adapter: ReadingsListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("HistoryFragment", "onViewCreated: IN CREATE VIEW")


        val observer = viewModel.getDataStream()
        observer.observe(this, object : Observer<List<Data>> {
            override fun onChanged(t: List<Data>?) {
                t?.let {
                    if (adapter == null) {
                        adapter = ReadingsListAdapter(it.reversed(), viewModel.isMetric(), context!!)
                        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        val divider = DividerItemDecoration(context, layoutManager.orientation)
                        readings_recycler_view.addItemDecoration(divider)
                        readings_recycler_view.layoutManager = layoutManager
                        readings_recycler_view.adapter = adapter
                    } else {
                        adapter?.newData(it.reversed())
                    }
                }
            }

        })
    }


}
