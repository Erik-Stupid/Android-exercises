package com.example.adsexercise

import android.app.Activity
import android.widget.Button
import android.widget.TextView

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

// Remove the line below after defining your own ad unit ID.
private const val TOAST_TEXT = "Test ads are being shown. " +
        "To show live ads, replace the ad unit ID in res/values/strings.xml " +
        "with your own ad unit ID."
//ca-app-pub-6316790267704283/2404831247
//ca-app-pub-6316790267704283/7982066333
private const val START_LEVEL = 1

class MainActivity : AppCompatActivity() {

    private var currentLevel: Int = 0
    private var interstitialAd: InterstitialAd? = null
    private lateinit var levelTextView: TextView
    lateinit var mAdView : AdView
    private lateinit var rewardedAd: RewardedAd
    private lateinit var myButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        
        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show()

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        rewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")
        val adLoadCallback = object: RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                Log.d("TAG", "Ad successfully loaded.")// Ad successfully loaded.
            }
            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                Log.d("TAG", "Ad failed to load.")// Ad failed to load.
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)

        myButton = findViewById(R.id.ad_button)
        myButton.setOnClickListener {
            if (rewardedAd.isLoaded) {
                val activityContext: Activity = this@MainActivity
                val adCallback = object: RewardedAdCallback() {
                    override fun onRewardedAdOpened() {
                        Log.d("TAG", "Opened")// Ad opened.
                    }
                    override fun onRewardedAdClosed() {
                        Log.d("TAG", "Closed")// Ad closed.
                    }
                    override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                        Log.d("TAG", "Earned 10 dolans!")// User earned reward.
                    }
                    override fun onRewardedAdFailedToShow(errorCode: Int) {
                        Log.d("TAG", "Ad failed to display")// Ad failed to display.
                    }
                }
                rewardedAd.show(activityContext, adCallback)
            }
            else {
                Log.d("TAG", "The rewarded ad wasn't loaded yet.")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.action_settings -> true
                else -> super.onOptionsItemSelected(item)
            }
}