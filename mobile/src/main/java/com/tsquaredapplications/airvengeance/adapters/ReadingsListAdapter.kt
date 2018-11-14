package com.tsquaredapplications.airvengeance.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tsquaredapplications.airvengeance.R
import com.tsquaredapplications.airvengeance.data.Data
import java.util.*

class ReadingsListAdapter(var readingsList: List<Data>, private val isCelsius: Boolean = true, val context: Context) : RecyclerView.Adapter<ReadingsListAdapter.ReadingsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.readings_list_item, parent, false)
        return ReadingsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return readingsList.size
    }

    override fun onBindViewHolder(holder: ReadingsViewHolder, position: Int) {
        val currentItem = readingsList[position]
        val cal = Calendar.getInstance()
        cal.time = Date(currentItem.timestamp)
        val dateString = "${cal.get(Calendar.MONTH)}/${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.YEAR)}"
        holder.dateTv.text = dateString

        val timeString = "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}"
        holder.timeTv.text = timeString

        holder.temperatureTv.text = context.getString(R.string.temperature_reading, currentItem.temp.toString(), (if (isCelsius) 'C' else 'F'))
        holder.humidityTv.text = context.getString(R.string.humidity_reading, currentItem.humidity.toString())
        holder.pressureTv.text = context.getString(R.string.pressure_reading, currentItem.pressure.toString())
        holder.pm25Tv.text = context.getString(R.string.pm25_reading, currentItem.pm25.toString())
        holder.pm10Tv.text = context.getString(R.string.pm10_reading, currentItem.pm10.toString())

    }

    inner class ReadingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dateTv: TextView = itemView.findViewById(R.id.date_tv)
        val timeTv: TextView = itemView.findViewById(R.id.time_tv)

        val temperatureTv: TextView = itemView.findViewById(R.id.temperature_reading_tv)
        val humidityTv: TextView = itemView.findViewById(R.id.humidity_reading_tv)
        val pressureTv: TextView = itemView.findViewById(R.id.pressure_reading_tv)
        val pm25Tv: TextView = itemView.findViewById(R.id.pm25_reading_tv)
        val pm10Tv: TextView = itemView.findViewById(R.id.pm10_reading_tv)
    }



    fun newData(dataList: List<Data>){
        readingsList = dataList
        notifyItemChanged(0)
    }
}