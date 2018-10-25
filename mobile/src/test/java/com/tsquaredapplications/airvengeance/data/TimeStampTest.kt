package com.tsquaredapplications.airvengeance.data

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class TimeStampTest{

    private lateinit var time1: TimeStamp
    private lateinit var time2: TimeStamp
    private lateinit var time3: TimeStamp

    @Before
    fun setup(){
        time1 = TimeStamp(1, 1, 2018, 1, 13, 0) // 1/1/2018 1:13 am
        time2 = TimeStamp(1,2,2018, 15, 45, 0) // 2/1/2018 3:45 pm
        time3 = TimeStamp(1,1,2018, 1, 16, 0) // 1/1/2018 1:16 am
    }

    @Test
    fun `compare timestamp with different month`(){
        val result = time1.compareTo(time2)
        assertEquals(-1, result)
    }

    @Test
    fun `compare timestamp with same day different time`(){
        val result = time3.compareTo(time1)
        assertEquals(1, result)
    }

    @Test
    fun `time display before noon`(){
        val expected = "1:13 am"
        val actual = TimeStamp.getTimeDisplay(time1)
        assertEquals(expected, actual)
    }

    @Test
    fun `time display after noon`(){
        val expected = "3:45 pm"
        val actual = TimeStamp.getTimeDisplay(time2)
        assertEquals(expected, actual)
    }
}