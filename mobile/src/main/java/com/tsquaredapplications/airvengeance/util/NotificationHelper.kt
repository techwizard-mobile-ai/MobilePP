package com.tsquaredapplications.airvengeance.R.NotificationHelper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.tsquaredapplications.airvengeance.R

@RequiresApi(Build.VERSION_CODES.O)
internal class NotificationHelper

(ctx: Context) : ContextWrapper(ctx) {
    private val manager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun sendNotification(title: String, text: String){
        val channel = NotificationChannel("default", "notificationChannel", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
        var notification = Notification.Builder(applicationContext,"default")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_stat_name)
        manager.notify(1,notification.build())
    }
}
