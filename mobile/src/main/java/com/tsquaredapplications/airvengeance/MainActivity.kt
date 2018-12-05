package com.tsquaredapplications.airvengeance


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tsquaredapplications.airvengeance.R.NotificationHelper.NotificationHelper
import com.tsquaredapplications.airvengeance.data.Data
import com.tsquaredapplications.airvengeance.data.DataRepository
import com.tsquaredapplications.airvengeance.util.FirebaseUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import android.content.Intent
import android.view.View
import com.tsquaredapplications.airvengeance.presenters.SensorManager


class MainActivity : AppCompatActivity() {

    val navController by lazy { findNavController(R.id.nav_host) }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()

        ///////////////////////////////////////////////
        /*Notifications Code*/
        var fireTemp: Int = 0
        var firePress: Int = 0
        var fireHumid: Int = 0

        val task = Executors.newScheduledThreadPool(5)
        task.scheduleAtFixedRate(Runnable {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)

            //ALL USER PREFERENCE VALUES
            var minTemp: Int
            var maxTemp: Int
            var minPress: Int
            var maxPress: Int
            var minHumid: Int
            var maxHumid: Int

            minTemp = prefs.getString(getString(R.string.minimum_temp), "10").toInt()
            maxTemp = prefs.getString(getString(R.string.maximum_temp), "15").toInt()
            minPress = prefs.getString(getString(R.string.minimum_press), "15").toInt()
            maxPress = prefs.getString(getString(R.string.maximum_press), "20").toInt()
            minHumid = prefs.getString(getString(R.string.minimum_humid), "20").toInt()
            maxHumid = prefs.getString(getString(R.string.maximum_humid), "40").toInt()

            //Firebase Values Below

            var dataRepo = DataRepository()
            var dataStream = dataRepo.getDataStream()
            val db = FirebaseUtil.openDataRef()
            db.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // Do nothing
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val data = mutableListOf<Data>()
                    val dataChildren = p0.children
                    for (child in dataChildren) {
                        val possibleData = child.getValue(Data::class.java)
                        possibleData?.let { nonNullData ->
                            data.add(nonNullData)
                        }
                    }

                    val recentVals = data[data.size - 1]

                    fireTemp = recentVals.temp!!.toInt()
                    firePress = recentVals.pressure!!.toInt()
                    fireHumid = recentVals.humidity!!.toInt()

                }

            })


            //Notifications Setup
            var nManager = NotificationHelper(this)


            //Comparing/Sending Notifications

            if (fireTemp < minTemp) { //below temp
                nManager.sendNotification("Air Quality Alert", "Air Temperature is too Cold!", "amazon.com")
            }
            if (fireTemp > maxTemp) {//above temp
                nManager.sendNotification("Air Quality Alert", "Air Temperature is too Hot!", "amazon.com")
            }
            if (firePress < minPress) {//below pressure
                nManager.sendNotification("Air Quality Alert", "Air Pressure is too Low!", "amazon.com")
            }
            if (firePress > maxPress) {//above pressure
                nManager.sendNotification("Air Quality Alert", "Air Pressure is too High!", "amazon.com")
            }

            if (fireHumid < minHumid) {//below humidity
                nManager.sendNotification("Air Quality Alert", "Air Humidity is too Dry!", "amazon.com")
            }
            if (fireHumid > maxHumid) {//above humditiy
                nManager.sendNotification("Air Quality Alert", "Air Humidity is too Humid!", "amazon.com")
            }

        },0,5, TimeUnit.SECONDS)

    }
    /*End Notifications Code*/
    ////////////////////////////////////////


    private fun setupNavigation() {
        NavigationUI.setupWithNavController(bottom_navigation, navController)
    }

    // Attempt to fix up navigation
    override fun onSupportNavigateUp() =
            navController.navigateUp() || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_settings -> {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
        return true
    }

    fun openManager(view: View){
        val intent = Intent(this, SensorManager::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

