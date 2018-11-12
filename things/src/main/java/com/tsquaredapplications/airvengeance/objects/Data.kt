package com.tsquaredapplications.airvengeance.objects

import java.util.*

class Data(val temp: Float? = 0.0f,
           val humidity: Float? = 0.0f,
           val pressure: Float? = 0.0f,
           val pm25: Int? = 0,
           val pm10: Int? = 0,
           val timestamp: Long = Calendar.getInstance().timeInMillis)