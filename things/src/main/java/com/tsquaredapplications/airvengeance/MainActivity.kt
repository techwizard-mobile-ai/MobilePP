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
import com.tsquaredapplications.airvengeance.objects.Data
import com.tsquaredapplications.airvengeance.objects.Repository
import java.util.concurrent.TimeUnit


private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity() {

    companion object {
        private const val SAMPLE_INTERVAL_MS = 10000
        private const val BMX280_I2C_BUS_NAME = "I2C1"
        private const val HPM_SENSOR_UART_NAME = "UART0"
    }

    private val supportedSensors = HashSet<Int>()
    lateinit var bmx280SensorDriver: Bmx280SensorDriver
    lateinit var hpmDriver: HpmSensorDriver

    // Instance of sensor manager
    lateinit var sensorManager: SensorManager
    lateinit var handler: Handler
    private var doSampleToken = Any()

    // Repository for Firebase DB interaction
    lateinit var repo: Repository

    inner class SensorData {
        internal var temperature: Float = 0.toFloat()
        internal var temperatureTimestamp: Long = 0

        internal var humidity: Float = 0.toFloat()
        internal var humidityTimestamp: Long = 0

        internal var pressure: Float = 0.toFloat()
        internal var pressureTimestamp: Long = 0

        var pm25: Int = 0
        var pm10: Int = 0
        internal var particleTimestamp: Long = 0
    }

    var mSensorData: SensorData = SensorData()
    var mSensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            when (event?.sensor?.type) {
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    mSensorData.temperature = event.values[0]
                    mSensorData.temperatureTimestamp = event.timestamp

                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    mSensorData.humidity = event.values[0]
                    mSensorData.humidityTimestamp = event.timestamp
                }
                Sensor.TYPE_PRESSURE -> {
                    mSensorData.pressure = event.values[0]
                    mSensorData.pressureTimestamp = event.timestamp
                }
                Sensor.TYPE_DEVICE_PRIVATE_BASE -> {
                    if (HpmSensorDriver.SENSOR_STRING_TYPE.equals(event.sensor.stringType)) {
                        mSensorData.pm25 = event.values[0].toInt()
                        mSensorData.pm10 = event.values[1].toInt()
                        mSensorData.particleTimestamp = event.timestamp
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportedSensors.add(Sensor.TYPE_AMBIENT_TEMPERATURE)
        supportedSensors.add(Sensor.TYPE_RELATIVE_HUMIDITY)
        supportedSensors.add(Sensor.TYPE_PRESSURE)
        supportedSensors.add(Sensor.TYPE_DEVICE_PRIVATE_BASE)

        repo = Repository()

        handler = Handler()
        registerSensors()
        startDataCollection()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopDataCollection()
        unregisterSensors()
    }

    private fun registerSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.registerDynamicSensorCallback(object : SensorManager.DynamicSensorCallback() {
            override fun onDynamicSensorConnected(sensor: Sensor) {
                if (supportedSensors.contains(sensor.type)) {
                    sensorManager.registerListener(mSensorEventListener, sensor,
                            SensorManager.SENSOR_DELAY_NORMAL)
                }
            }
        })

        // Register Temperature, Humidity, and Pressure sensor
        try {
            bmx280SensorDriver = Bmx280SensorDriver(BMX280_I2C_BUS_NAME)
            bmx280SensorDriver.registerTemperatureSensor()
            bmx280SensorDriver.registerHumiditySensor()
            bmx280SensorDriver.registerPressureSensor()
        } catch (e: IOException) {
            Log.e(TAG, "Error registering BMX280 sensor")
        }


        // Register HPM particle sensor driver
        try {
            hpmDriver = HpmSensorDriver(HPM_SENSOR_UART_NAME)
            hpmDriver.registerParticleSensor()
        } catch (e: IOException) {
            Log.e(TAG, "Error registering HPM sensor driver\n" + e.stackTrace)
        }
    }

    private fun unregisterSensors() {
        sensorManager.unregisterListener(mSensorEventListener)

        bmx280SensorDriver.unregisterTemperatureSensor()
        bmx280SensorDriver.unregisterHumiditySensor()
        bmx280SensorDriver.unregisterPressureSensor()
        bmx280SensorDriver.close()

        hpmDriver.unregisterParticleSensor()
        hpmDriver.close()

    }


    private fun startDataCollection() {
        val doDataCollection = object : Runnable {
            private fun toOld(timestamp: Long): Boolean {
                val timestampMs = TimeUnit.NANOSECONDS.toMillis(timestamp)
                return SystemClock.uptimeMillis() - timestampMs > SAMPLE_INTERVAL_MS
            }

            override fun run() {
                // Record a null reading if the sensor data is too old.
                val temperature = (if (toOld(mSensorData.temperatureTimestamp))
                    null
                else
                    mSensorData.temperature)?.toFloat()
                val humidity = (if (toOld(mSensorData.humidityTimestamp))
                    null
                else
                    mSensorData.humidity)?.toFloat()
                val pressure = (if (toOld(mSensorData.pressureTimestamp))
                    null
                else
                    mSensorData.pressure / 33.863886666667)?.toFloat()
                val pm25 = (if (toOld(mSensorData.particleTimestamp))
                    null
                else
                    mSensorData.pm25)?.toInt()
                val pm10 = (if (toOld(mSensorData.particleTimestamp))
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
                        "\tTemperature: %.1f, Humidity: %.1f%%, Pressure: %.1finHg\n" +
                        "\tPM2.5, PM10: %d, %d\n",
                        temperature,
                        humidity,
                        pressure,
                        pm25,
                        pm10))

                repo.push(Data(temperature, humidity, pressure, pm25, pm10))


                handler.postAtTime(this, doSampleToken,
                        SystemClock.uptimeMillis() + SAMPLE_INTERVAL_MS)
            }
        }
        handler.postAtTime(doDataCollection, doSampleToken,
                SystemClock.uptimeMillis() + SAMPLE_INTERVAL_MS)
    }

    private fun stopDataCollection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
