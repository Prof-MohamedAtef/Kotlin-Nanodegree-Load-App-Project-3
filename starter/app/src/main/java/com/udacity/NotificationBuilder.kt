package com.udacity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.ui.DetailActivity

class NotificationBuilder {
    companion object{

        private val ID_NOTIFICATION=0
        private val CHANNEL_ID: String="CHANNEL_ID_1"

        fun send(fileName: String, status: String, mContext: Context){
            createChannel(mContext, CHANNEL_ID, "MyChannel")
            val detailIntent= Intent(mContext, DetailActivity::class.java)
            detailIntent.putExtra(mContext.getString(R.string.file_name),fileName)
            detailIntent.putExtra(mContext.getString(R.string.status_), status)
            val pendingIntent= PendingIntent.getActivity(
                mContext,
                0,
                detailIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder= NotificationCompat.Builder(
                mContext,
                CHANNEL_ID
            ).setSmallIcon(R.drawable.ic_download)
                .setContentTitle(mContext.getString(R.string.notification_title))
                .setContentText("Download of ${fileName} is completed")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).addAction(
                    R.drawable.ic_download,
                    mContext
                        .getString(R.string.notification_button),
                    pendingIntent
                ).setPriority(Notification.PRIORITY_HIGH)

            val notificationMgr= ContextCompat.getSystemService(
                mContext, NotificationManager::class.java
            ) as NotificationManager

            notificationMgr.notify(0, builder.build())
        }

        fun createChannel(context: Context, channelId: String, channelName: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel= NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                channel.enableLights(true)
                channel.lightColor= Color.RED
                channel.enableVibration(true)
                channel.description=context.getString(R.string.notification_description)

                val notificationManager=context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}