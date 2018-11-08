package com.tsquaredapplications.airvengeance.presenters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.tsquaredapplications.airvengeance.R


class ReadingStatsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val readingType = ReadingStatsFragmentArgs.fromBundle(arguments).readingType
        Toast.makeText(context, "ARgs is $readingType", Toast.LENGTH_LONG).show()
    }


}
