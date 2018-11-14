package com.tsquaredapplications.airvengeance.adapters

import com.robinhood.spark.SparkAdapter

class SparkLineAdapter(val dataList: List<Float>): SparkAdapter() {
    override fun getY(index: Int): Float {
        return dataList[index]
    }

    override fun getItem(index: Int): Any {
        return dataList[index]
    }

    override fun getCount(): Int {
        return dataList.size
    }
}