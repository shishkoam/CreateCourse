package ua.kblogika.interactive.utils.interactors

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ua.shishkoam.createcourse.R

object NotificationHelper {
    private const val CHANNEL_ID = "NOTIFICATION"
    private const val CHANNEL_NAME = "Notification"
    private const val CHANNEL_DESCRIPTION = "Notification from app"
    private const val NOTIFICATION_ID = 181

    //cls example  AlertDetails::class.java
    fun showNotification(
        context: Context,
        title: String,
        message: String,
        cls: Class<*>? = null,
        broadcast: String? = null
    ) {
        // Create an explicit intent for an Activity in your app
        var pendingIntent: PendingIntent? = null
        cls?.let {
            val intent = Intent(context, cls).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        broadcast?.let{
            val updateIntent = Intent(broadcast)
            pendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT
            )
        }


        val builder = getNotificationBuilder(context, title, message)
        pendingIntent?.let {
            builder.setContentIntent(pendingIntent)

        }
        val manager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getNotificationBuilder(
        context: Context,
        title: String,
        message: String
    ): NotificationCompat.Builder {


        var channelId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(context, CHANNEL_NAME)
        }
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_info_blue_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //            builder.setCategory(Notification.CATEGORY_SERVICE)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        return builder
    }

    private fun cancelNotify(context: Context) {
        val manager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, channelName: String): String {
        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.run {
            lightColor = Color.BLUE
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = CHANNEL_DESCRIPTION
        }
        val service =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return CHANNEL_ID
    }
}
