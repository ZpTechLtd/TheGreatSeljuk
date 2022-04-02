package com.zptech.the.great.seljuk.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.applovin.sdk.AppLovinSdk
import com.google.android.material.navigation.NavigationView
import com.zptech.the.great.seljuk.*
import com.zptech.the.great.seljuk.adapter.RingToneAdapter
import com.zptech.the.great.seljuk.ads.AdsManager
import com.zptech.the.great.seljuk.databinding.ActivityMainBinding
import com.zptech.the.great.seljuk.model.RingTone


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var list: MutableList<RingTone>
    private lateinit var adapter: RingToneAdapter
    private val adsManager = AdsManager(this)
//    private var appUpdateManager: AppUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding =
            DataBindingUtil.setContentView(
                this@MainActivity,
                R.layout.activity_main
            )

        activityMainBinding.recyclerview.setHasFixedSize(true)
        activityMainBinding.recyclerview.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        activityMainBinding.recyclerview.addItemDecoration(
            ItemDecorator(
                resources.getDimensionPixelOffset(
                    R.dimen._10sdp
                )
            )
        )

        (activityMainBinding.recyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        list = getList()


        adapter = RingToneAdapter(list, this@MainActivity)
        activityMainBinding.recyclerview.adapter = adapter

        activityMainBinding.menuIcon.setOnClickListener {
            activityMainBinding.drawer.openDrawer(GravityCompat.START)
        }

        activityMainBinding.navigationView.setNavigationItemSelectedListener(this)
        activityMainBinding.navigationView.itemIconTintList = null

        adsManager.loadMaxInterstitial()
        adsManager.loadBannerMAxMediation(activityMainBinding.relTopBanner)
        adsManager.loadNativeBannerMax(findViewById(R.id.nativeSmall),activityMainBinding.relAd)
//        appUpdateManager = AppUpdateManagerFactory.create(this)
//        InAppUpdate.setImmediateUpdate(appUpdateManager, this)
//        AppLovinSdk.getInstance( this ).showMediationDebugger();
    }

    override fun onBackPressed() {
        if (activityMainBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        activityMainBinding.drawer.closeDrawer(GravityCompat.START)
        if (item.itemId == R.id.share) {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Share"))
            } catch (e: java.lang.Exception) {
                //e.toString();
            }

        } else if (item.itemId == R.id.privacy_policy) {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sites.google.com/view/zp-tech-naat-ringtones/home")
            )
            startActivity(browserIntent)
        }else if (item.itemId == R.id.rateus) {
            val pck: String = getPackageName()
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$pck")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$pck")
                    )
                )
            }
        }
        return true
    }

    fun showAd() {
        adsManager.showInterstitial()
    }

//    override fun onResume() {
//        super.onResume()
//        InAppUpdate.setImmediateUpdateOnResume(appUpdateManager, this);
//    }
}