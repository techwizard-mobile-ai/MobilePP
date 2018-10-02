package com.tsquaredapplications.airvengeance

import android.app.Activity
import android.content.Context
import android.hardware.Sensor

import android.os.Bundle
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver
import android.hardware.SensorManager
import com.tsquaredapplications.airvengeance.hpm_driver.HpmSensorDriver
import android.util.Log
import java.io.IOException
import android.os.SystemClock
import android.os.Handler
import com.google.android.things.pio.PeripheralManager
import java.util.concurrent.TimeUnit


private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity() {

    private val SUPPORTED_SENSORS = HashSet<Int>()
    var mBmx280SensorDriver: Bmx280SensorDriver? = null
    var mHpmDriver: HpmSensorDriver? = null

    // Instance of sensor manager
    private var mSensorManager: SensorManager? = null
    private var mHandler: Handler? = null
    private var mDoSampleToken = Any()

    private val SAMPLE_INTERVAL_MS = 10000
    private val BMX280_I2C_BUS_NAME = "I2C1"
    private val HPM_SENSOR_UART_NAME = "UART1"

    inner class SensorData {


        internal var temperature: Float = 0.toFloat()
        internal var temperature_timestamp: Long = 0

        internal var humidity: Float = 0.toFloat()
        internal var humidity_timestamp: Long = 0

        internal var pressure: Float = 0.toFloat()
        internal var pressure_timestamp: Long = 0

        var pm25: Int = 0
        var pm10: Int = 0
        internal var particle_timestamp: Long = 0
    }

    var mSensorData: SensorData = SensorData()
    var mSensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            when (event?.sensor?.type) {
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    mSensorData.temperature = event.values[0]
                    mSensorData.temperature_timestamp = event.timestamp

                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    mSensorData.humidity = event.values[0]
                    mSensorData.humidity_timestamp = event.timestamp
                }
                Sensor.TYPE_PRESSURE -> {
                    mSensorData.pressure = event.values[0]
                    mSensorData.pressure_timestamp = event.timestamp
                }
                Sensor.TYPE_DEVICE_PRIVATE_BASE -> {
                    if (HpmSensorDriver.SENSOR_STRING_TYPE.equals(event.sensor.stringType)) {
                        mSensorData.pm25 = event.values[0].toInt()
                        mSensorData.pm10 = event.values[1].toInt()
                        mSensorData.particle_timestamp = event.timestamp
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Log.i("MainActivity", "onCreate: IN ON CREATE")
        val manager = PeripheralManager.getInstance()

        SUPPORTED_SENSORS.add(Sensor.TYPE_AMBIENT_TEMPERATURE)
        SUPPORTED_SENSORS.add(Sensor.TYPE_RELATIVE_HUMIDITY)
        SUPPORTED_SENSORS.add(Sensor.TYPE_PRESSURE)
        SUPPORTED_SENSORS.add(Sensor.TYPE_DEVICE_PRIVATE_BASE)
        mHandler = Handler()
        registerSensors()

        startDataCollection()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopDataCollection()
        unregisterSensors()
    }

    private fun registerSensors() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mSensorManager?.registerDynamicSensorCallback(object : SensorManager.DynamicSensorCallback() {
            override fun onDynamicSensorConnected(sensor: Sensor) {
                if (SUPPORTED_SENSORS.contains(sensor.type)) {
                    mSensorManager?.registerListener(mSensorEventListener, sensor,
                            SensorManager.SENSOR_DELAY_NORMAL)
                }
            }
        })

        // Register Temperature, Humidity, and Pressure sensor
        try {
            mBmx280SensorDriver = Bmx280SensorDriver(BMX280_I2C_BUS_NAME)
            mBmx280SensorDriver?.registerTemperatureSensor()
            mBmx280SensorDriver?.registerHumiditySensor()
            mBmx280SensorDriver?.registerPressureSensor()
        } catch (e: IOException) {
            Log.e(TAG, "Error registering BMX280 sensor")
        }


        // Register HPM particle sensor driver
        try {
            mHpmDriver = HpmSensorDriver(HPM_SENSOR_UART_NAME)
            mHpmDriver?.registerParticleSensor()
        } catch (e: IOException) {
            Log.e(TAG, "Error registering HPM sensor driver")
        }
    }

    private fun unregisterSensors() {
        mSensorManager?.unregisterListener(mSensorEventListener)
        if (mBmx280SensorDriver != null) {
            mBmx280SensorDriver?.unregisterTemperatureSensor()
            mBmx280SensorDriver?.unregisterHumiditySensor()
            mBmx280SensorDriver?.unregisterPressureSensor()
            try {
                mBmx280SensorDriver?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error closing BMX280 sensor")
            }

        }
        if (mHpmDriver != null) {
            mHpmDriver?.unregisterParticleSensor()
            try {
                mHpmDriver?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error closing GPS driver")
            }

        }
    }

    private fun startDataCollection() {
        val doDataCollection = object : Runnable {
            private fun toOld(timestamp: Long): Boolean {
                val timestamp_ms = TimeUnit.NANOSECONDS.toMillis(timestamp)
                return SystemClock.uptimeMillis() - timestamp_ms > SAMPLE_INTERVAL_MS
            }

            override fun run() {
                // Record a null reading if the sensor data is too old.
                val temperature = (if (toOld(mSensorData.temperature_timestamp))
                    null
                else
                    mSensorData.temperature)?.toFloat()
                val humidity = (if (toOld(mSensorData.humidity_timestamp))
                    null
                else
                    mSensorData.humidity)?.toFloat()
                val pressure = (if (toOld(mSensorData.pressure_timestamp))
                    null
                else
                    mSensorData.pressure)?.toFloat()
                val pm25 = (if (toOld(mSensorData.particle_timestamp))
                    null
                else
                    mSensorData.pm25)?.toInt()
                val pm10 = (if (toOld(mSensorData.particle_timestamp))
                    null
                else
                    mSensorData.pm10)?.toInt()

                // TODO: Fix time issue in next developer preview.
                //
                // There isn't a *good* way to set the system time in the latest developer
                // preview (DP5.1), if the device is offline:
                //     https://issuetracker.google.com/issues/64426912
                //
                // Since our device may not have network connectivity for extended periods of time
                // (and with possible reboots in between), we need some mechanism of getting the
                // time without network connectivity.  Otherwise the system time will be close to
                // January 1st, 2009 00:00 UTC
                //
                // So, we log the time delivered in the location update (from the GPS), and only
                // log data using these timestamps.  This is fine, since we're only interested in
                // logging data when we have a GPS fix anyway.
                //
                // The semantics of the what the time returned in location updates should be
                // is somewhat confusing.  In practice, we will get a usable time because of the
                // way we're using Location Services with GPS as a source:
                //     https://stackoverflow.com/questions/7017069/gps-time-in-android;

                Log.d(TAG, String.format("Logged\n" +
                        "\tTemperature: %.1f, Humidity: %.1f%%, Pressure: %.1fhPa\n" +
                        "\tPM2.5, PM10: %d, %d\n",
                        temperature,
                        humidity,
                        pressure,
                        pm25,
                        pm10))

                mHandler?.postAtTime(this, mDoSampleToken,
                        SystemClock.uptimeMillis() + SAMPLE_INTERVAL_MS)
            }
        }
        mHandler?.postAtTime(doDataCollection, mDoSampleToken,
                SystemClock.uptimeMillis() + SAMPLE_INTERVAL_MS)
    }

    private fun stopDataCollection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
