package com.zptech.the.great.seljuk.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.zptech.the.great.seljuk.PreferenceManager
import com.zptech.the.great.seljuk.R
import com.zptech.the.great.seljuk.ads.AdsManager


class SplashActivity : AppCompatActivity() {

    private var handlerConfiguration: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

       val preferenceManager= PreferenceManager(this@SplashActivity)
        val manager =
            AdsManager(this@SplashActivity)
        manager.loadNativeLarge(findViewById(R.id.fl_adplaceholder))
//        manager.loadInterstitialSplash()


        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        FirebaseMessaging.getInstance().subscribeToTopic(packageName)
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetch(0)
            .addOnSuccessListener(OnSuccessListener<Void?> {
                remoteConfig.fetchAndActivate()
                handlerConfiguration = Handler(Looper.getMainLooper())
                handlerConfiguration!!.postDelayed(Runnable {

                    if (!TextUtils.isEmpty(remoteConfig.getString("RINGTONE_INTERSTITIAL"))) {
                        preferenceManager.admobInterstitial =
                            remoteConfig.getString("RINGTONE_INTERSTITIAL")
                    }

                    if (!TextUtils.isEmpty(remoteConfig.getString("RINGTONE_OPEN_APP"))){
                        preferenceManager.admobOpen =
                            remoteConfig.getString("RINGTONE_OPEN_APP")
                    }


                    if (!TextUtils.isEmpty(remoteConfig.getString("RINGTONE_NATIVE_LARGE"))){
                        preferenceManager.admoB_native =
                            remoteConfig.getString("RINGTONE_NATIVE_LARGE")
                    }

                    if (!TextUtils.isEmpty(remoteConfig.getString("RINGTONE_BANNER"))){
                        preferenceManager.admoB_native_banner =
                            remoteConfig.getString("RINGTONE_BANNER")
                    }

                    if (!TextUtils.isEmpty(remoteConfig.getString("MAX_INTERSTITIAL"))){
                        preferenceManager.maxInterstitial =
                            remoteConfig.getString("MAX_INTERSTITIAL")
                    }

                    if (!TextUtils.isEmpty(remoteConfig.getString("MAX_NATIVE_SMALL"))){
                        preferenceManager.maxNativeSmall =
                            remoteConfig.getString("MAX_NATIVE_SMALL")
                    }

                    if (!TextUtils.isEmpty(remoteConfig.getString("max_banner"))){
                        preferenceManager.maxBanner =
                            remoteConfig.getString("max_banner")
                    }

                }, 3000)
            })

        Handler(Looper.getMainLooper()).postDelayed({
            if (preferenceManager.privacyPolicy) {
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            }
//            manager.showInterstitialSplash()
            finish()
        }, 8000)
    }
}