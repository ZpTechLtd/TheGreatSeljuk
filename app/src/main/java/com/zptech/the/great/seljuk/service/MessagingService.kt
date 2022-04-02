package com.zptech.the.great.seljuk.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.atomic.AtomicInteger

class MessagingService : FirebaseMessagingService() {

    companion object {
        const val ICON_KEY = "icon"
        const val APP_TITLE_KEY = "title"
        const val SHORT_DESC_KEY = "short_desc"
        const val LONG_DESC_KEY = "long_desc"
        const val APP_FEATURE_KEY = "feature"
        const val APP_URL_KEY = "app_url"
        private val seed = AtomicInteger()

        fun getNextInt(): Int {
            return seed.incrementAndGet()
        }
    }

    override fun onNewToken(token: String) {
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        if (data != null && !data.isEmpty()) {
            NotificationUtils.sendAppInstallNotification(this, data)
        }

    }
}