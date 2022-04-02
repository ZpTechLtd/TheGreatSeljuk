package com.zptech.the.great.seljuk.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.*
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.squareup.picasso.Picasso
import com.zptech.the.great.seljuk.R


object NotificationUtils {

    fun sendAppInstallNotification(context: Context?,data: Map<String, String>?): Boolean {
        if (context == null) return false

        val iconURL = data?.get(MessagingService.ICON_KEY)
        val title = data?.get(MessagingService.APP_TITLE_KEY)
        val shortDesc = data?.get(MessagingService.SHORT_DESC_KEY)
        val longDesc = data?.get(MessagingService.LONG_DESC_KEY)
        val feature = data?.get(MessagingService.APP_FEATURE_KEY)
        val appURL = data?.get(MessagingService.APP_URL_KEY)

        val notificationID = MessagingService.getNextInt()

        if (iconURL != null && title != null && shortDesc != null && feature != null && appURL != null) {
            val standard = "https://play.google.com/store/apps/details?id="

            try {
                val id = appURL.substring(standard.length)

                if (!isAppInstalled(id, context))
                    Handler(context.mainLooper).post {


                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appURL))
                        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                        val remoteViews = RemoteViews(context.packageName, R.layout.notification_app)

                        remoteViews.setTextViewText(R.id.tv_title, title)
                        remoteViews.setTextViewText(R.id.tv_short_desc, shortDesc)
                        remoteViews.setTextViewText(R.id.tv_long_desc, longDesc)
                        remoteViews.setViewVisibility(R.id.tv_long_desc, if (longDesc != null && !longDesc.isEmpty()) View.VISIBLE else View.GONE)

                        val builder = NotificationCompat.Builder(context, title)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.drawable.ic_ad_small)
                                .setContentIntent(pendingIntent)
                                .setOnlyAlertOnce(true)
                                .setAutoCancel(true)
                                .setCustomContentView(remoteViews)
                                .setCustomBigContentView(remoteViews)

                        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val channel = NotificationChannel(title,
                                    "Channel human readable title",
                                    NotificationManager.IMPORTANCE_DEFAULT)
                            mNotificationManager.createNotificationChannel(channel)
                        }

                        mNotificationManager.notify(notificationID, builder.build())
                        Picasso.get()
                                .load(iconURL)
                                .into(remoteViews, R.id.iv_icon, notificationID, builder.build())
                        Picasso.get()
                                .load(feature)
                                .into(remoteViews, R.id.iv_feature, notificationID, builder.build())


                    }
                return true
            } catch (e: Exception) {
            }
        }
        return false
    }


    private fun isAppInstalled(uri: String, context: Context): Boolean {
        val pm = context.packageManager
        return try {
            val applicationInfo = pm.getApplicationInfo(uri, 0)
            applicationInfo.enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }
}
