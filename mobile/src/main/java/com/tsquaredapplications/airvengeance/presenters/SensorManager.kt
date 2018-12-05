package com.tsquaredapplications.airvengeance.presenters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tsquaredapplications.airvengeance.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.R.id.screen
import androidx.core.content.ContextCompat

class SensorManager : AppCompatActivity() {
    lateinit var textView1 : TextView
    lateinit var textView2 : TextView
    lateinit var textView3 : TextView
    lateinit var textView4 : TextView
    lateinit var textID1 : TextView
    lateinit var textID2 : TextView
    lateinit var textID3 : TextView
    lateinit var textID4 : TextView
    lateinit var button1 : Button
    lateinit var button2 : Button
    lateinit var button3 : Button
    lateinit var button4 : Button
    private var sensorCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_manager)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        textView1= findViewById(R.id.sensor1) as TextView
        textView2= findViewById(R.id.sensor2) as TextView
        textView3= findViewById(R.id.sensor3) as TextView
        textView4= findViewById(R.id.sensor4) as TextView
        textID1= findViewById(R.id.sensor1ID) as TextView
        textID2= findViewById(R.id.sensor2ID) as TextView
        textID3= findViewById(R.id.sensor3ID) as TextView
        textID4 = findViewById(R.id.sensor4ID) as TextView
        button1 = findViewById(R.id.button1) as Button
        button2 = findViewById(R.id.button2) as Button
        button3 = findViewById(R.id.button3) as Button
        button4 = findViewById(R.id.button4) as Button
        button1.background.setColorFilter(ContextCompat.getColor(this,R.color.material_deep_teal_200),PorterDuff.Mode.MULTIPLY)
        button2.background.setColorFilter(ContextCompat.getColor(this,R.color.red),PorterDuff.Mode.MULTIPLY)
        button3.background.setColorFilter(ContextCompat.getColor(this,R.color.red),PorterDuff.Mode.MULTIPLY)
        button4.background.setColorFilter(ContextCompat.getColor(this,R.color.red),PorterDuff.Mode.MULTIPLY)
    }

    fun editSensor(view: View){
        //view is the box that has been tapped
        //set new title
        textPop("Please Enter Sensor Name",view as TextView)
        //then get the firebase Token
        //first, decide which sensorIDbox
        when(view) {//then set the new id within the box
            textView1 ->textPop("Please Enter Firebase Key",textID1)
            textView2 ->textPop("Please Enter Firebase Key",textID2)
            textView3 ->textPop("Please Enter Firebase Key",textID3)
            textView4 ->textPop("Please Enter Firebase Key",textID4)
        }
    }

    fun textPop(title : String, outputBox : TextView){
        //Provide function with a title for the dialog
        //and the TextView where you will output the entry
        var input : String = "test"
        var builder = AlertDialog.Builder(this)
        var textBox = EditText(this)

        builder.setTitle(title)
        builder.setView(textBox)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which -> input = textBox.text.toString() })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.setOnDismissListener({
            outputBox.setText(input)
        })
        builder.create()
        builder.show()
    }

    @SuppressLint("NewApi")
    fun changeSensor(view: View){
        button1.background.setColorFilter(ContextCompat.getColor(this,R.color.red),PorterDuff.Mode.MULTIPLY)
        button2.background.setColorFilter(ContextCompat.getColor(this,R.color.red),PorterDuff.Mode.MULTIPLY)
        button3.background.setColorFilter(ContextCompat.getColor(this,R.color.red),PorterDuff.Mode.MULTIPLY)
        button4.background.setColorFilter(ContextCompat.getColor(this,R.color.red),PorterDuff.Mode.MULTIPLY)

        var clickedButton = view as Button
        clickedButton.background.setColorFilter(ContextCompat.getColor(this,R.color.material_deep_teal_200),PorterDuff.Mode.MULTIPLY)

    }
}
