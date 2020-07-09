package com.dewonderstruck.apps.ashx0.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.dewonderstruck.apps.MainActivity
import com.dewonderstruck.apps.ashx0.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Vamsi Madduluri on 8/11/16.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

open class FirebaseMessagingService : FirebaseMessagingService() {
    private val channelId = "StoreChannelId1"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        /* Important : Please don't change this "message" because if you change this, need to update at PHP.  */
        var msg = remoteMessage.data["message"]

//        Utils.psLog("****** 2 >>> " + remoteMessage.getData().get("action2"));
        if (msg == null || msg == "") {
            msg = remoteMessage.notification!!.body
        }
        showNotification(msg)
    }

    private fun showNotification(message: String?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = pref.edit()
        editor.putString(Constants.NOTI_MSG, message)
        editor.putBoolean(Constants.NOTI_EXISTS_TO_SHOW, true)
        editor.apply()
        val i = Intent(applicationContext, MainActivity::class.java)
        i.putExtra(Constants.NOTI_MSG, message)
        val displayMessage: String
        Utils.psLog("Message From Server : $message")
        displayMessage = if (message == null || message == "") {
            i.putExtra(Constants.NOTI_EXISTS_TO_SHOW, false)
            "Welcome from Ashtrixx"
        } else {
            i.putExtra(Constants.NOTI_EXISTS_TO_SHOW, true)
            message //"You've received new message.";
        }

        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, Constants.ZERO)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.notification__alert))
                .setContentText(displayMessage)
                .setSmallIcon(R.drawable.ic_notifications_white)
                .setContentIntent(pendingIntent)

        // Set Vibrate, Sound and Light
        var defaults = 0
        defaults = defaults or Notification.DEFAULT_LIGHTS
        defaults = defaults or Notification.DEFAULT_VIBRATE
        defaults = defaults or Notification.DEFAULT_SOUND
        builder.setDefaults(defaults)
        // Set the content for Notification
        builder.setContentText(displayMessage)
        // Set autocancel
        builder.setAutoCancel(true)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = channelId
            val channel = NotificationChannel(channelId, getString(R.string.notification__alert), NotificationManager.IMPORTANCE_DEFAULT)
            manager?.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }
        manager?.notify(0, builder.build())
    }
}