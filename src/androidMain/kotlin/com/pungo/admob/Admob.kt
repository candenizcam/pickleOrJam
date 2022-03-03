package com.pungo.admob

import android.app.*
import android.view.*
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.soywiz.korio.android.*
import com.soywiz.korio.async.*
import com.soywiz.korio.lang.close
import com.soywiz.korge.view.Views
import kotlinx.coroutines.*


actual suspend fun AdmobCreate(views: Views, testing: Boolean): Admob {
    val activity = androidContext() as Activity
    android.util.Log.d("ADMOB", "AdmobCreate: Creating...")
    android.util.Log.d("ADMOB", activity.title as String)
    val res = AndroidAdmob(views, activity, testing)
    android.util.Log.d("ADMOB", "AdmobCreate: Created!")
    return res
}

private class AndroidAdmob(views: Views, val activity: Activity, val testing: Boolean) : Admob(views) {
    val initialRootView: android.view.View
    //val rootView: android.view.View

    init {
        android.util.Log.d("ADMOB", "AdmobCreate: Running init block...")
        initialRootView = activity.window.decorView.findViewById<android.view.View>(android.R.id.content).let {
            if (it is FrameLayout) it.getChildAt(0) else it
        }
        android.util.Log.d("ADMOB", "AdmobCreate: InitialRootView initialized.")
       /* rootView = if (initialRootView !is RelativeLayout) {
            //if (rootView !is LinearLayout) {
            android.util.Log.d("ADMOB", "AdmobCreate: InitialRootView is NOT relative.")
            RelativeLayout(activity).apply {
                initialRootView.removeFromParent()
                addView(
                    initialRootView,
                    RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                activity.setContentView(this)
            }
        } else {
            initialRootView
        }

        */
        android.util.Log.d("ADMOB", "AdmobCreate: RootView initialized.")
    }

    // Let's convert the rootView into a suitable format



    var interstitialAd: InterstitialAd? = null

    override suspend fun available() = true

    override suspend fun bannerPrepare(config: Admob.Config) {
        /*
        this.bannerAtTop = config.bannerAtTop
        activity.runOnUiThread {
            adView.adUnitId = if (testing) "ca-app-pub-3940256099942544/6300978111" else config.id
            adView.adSize = when (config.size) {
                Size.BANNER -> AdSize.BANNER
                Size.IAB_BANNER -> AdSize.BANNER
                Size.IAB_LEADERBOARD -> AdSize.BANNER
                Size.IAB_MRECT -> AdSize.BANNER
                Size.LARGE_BANNER -> AdSize.LARGE_BANNER
                Size.SMART_BANNER -> AdSize.SMART_BANNER
                Size.FLUID -> AdSize.FLUID
            }
        }
        activity.runOnUiThread {
            adView.loadAd(config.toAdRequest())
        }

         */
    }

    override suspend fun bannerShow() {
        /*
        activity.runOnUiThread {
            if (adView.parent != rootView) {
                adView.removeFromParent()
                rootView.addView(
                    adView,
                    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                        addRule(RelativeLayout.CENTER_HORIZONTAL)
                        addRule(if (bannerAtTop) RelativeLayout.ALIGN_PARENT_TOP else RelativeLayout.ALIGN_PARENT_BOTTOM)
                    }
                )
            }
            adView.visibility = View.VISIBLE
        }
        //activity.view

         */
    }

    override suspend fun bannerHide() {
        /*
        activity.runOnUiThread {
            adView.visibility = View.INVISIBLE
        }

         */
    }

    override suspend fun interstitialPrepare(config: Config) {
        activity.runOnUiThread {
            var adRequest = AdRequest.Builder().build()
            val id = if (testing) {
                "ca-app-pub-3940256099942544/1033173712"
            } else {
                config.id
            }
            InterstitialAd.load(activity, id, adRequest, object: InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(error: LoadAdError) {
                    // TODO error handling
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    interstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            //TODO error handling
                            android.util.Log.d("ADMOB", "Ad dismissed.")
                            config.actions.dismissAction()
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            android.util.Log.d("ADMOB", "Ad failed")
                            //TODO error handling
                        }

                        override fun onAdShowedFullScreenContent() {
                            interstitialAd = null
                            android.util.Log.d("ADMOB", "Ad showed.")
                        }

                        override fun onAdClicked() {
                            //TODO ad stuff maybe
                            android.util.Log.d("ADMOB", "Ad clicked.")
                        }

                        override fun onAdImpression() {
                            //TODO ad stuff maybe and also learn what this means
                            android.util.Log.d("ADMOB", "Ad impressed?")
                        }
                    }
                    interstitialAd!!.show(activity)
                }
            })

        }
    }

    suspend fun interstitialPrepare(adUnitID: String) {
        activity.runOnUiThread {
            var adRequest = AdRequest.Builder().build()
            val id = if (testing) {
                "ca-app-pub-3940256099942544/1033173712"
            } else {
                adUnitID
            }
            InterstitialAd.load(activity, id, adRequest, object: InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(error: LoadAdError) {
                    // TODO error handling
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    interstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            //TODO error handling
                            android.util.Log.d("ADMOB", "Ad dismissed.")
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            android.util.Log.d("ADMOB", "Ad failed")
                            //TODO error handling
                        }

                        override fun onAdShowedFullScreenContent() {
                            interstitialAd = null
                            android.util.Log.d("ADMOB", "Ad showed.")
                        }

                        override fun onAdClicked() {
                            //TODO ad stuff maybe
                            android.util.Log.d("ADMOB", "Ad clicked.")
                        }

                        override fun onAdImpression() {
                            //TODO ad stuff maybe and also learn what this means
                            android.util.Log.d("ADMOB", "Ad impressed?")
                        }
                    }
                    interstitialAd!!.show(activity)
                }
            })

        }
    }

    override suspend fun interstitialShowAndWait() {
        /*
        val result = mapOf(
            interstitialSignals.onAdClosed to "closed",
            interstitialSignals.onAdClicked to "clicked"
        ).executeAndWaitAnySignal {
            activity.runOnUiThread {
                interstitial.show()
            }
        }

         */
    }

    override suspend fun rewardvideolPrepare(config: Admob.Config) {
        /*
        activity.runOnUiThread {
            rewardVideo.userId = config.userId
            if (config.immersiveMode != null) {
                rewardVideo.setImmersiveMode(config.immersiveMode)
            }
            rewardVideo.loadAd(
                if (testing) "ca-app-pub-3940256099942544/5224354917" else config.id,
                config.toAdRequest()
            )
        }

         */
    }

   // override suspend fun rewardvideolIsLoaded(): Boolean = rewardVideo.isLoaded

    override suspend fun rewardvideoShowAndWait() {
        /*
        rewardVideoSignals.onRewardedVideoAdClosed.executeAndWaitSignal {
            activity.runOnUiThread {
                rewardVideo.show()
            }
        }

         */
    }
}

private suspend inline fun <T> Map<Signal<Unit>, T>.executeAndWaitAnySignal(callback: () -> Unit): T {
    val deferred = CompletableDeferred<T>()
    val closeables = this.map { pair -> pair.key.once { deferred.complete(pair.value) } }
    try {
        callback()
        return deferred.await()
    } finally {
        closeables.close()
    }
}

private suspend inline fun <T> Iterable<Signal<T>>.executeAndWaitAnySignal(callback: () -> Unit): Pair<Signal<T>, T> {
    val deferred = CompletableDeferred<Pair<Signal<T>, T>>()
    val closeables = this.map { signal -> signal.once { deferred.complete(signal to it) } }
    try {
        callback()
        return deferred.await()
    } finally {
        closeables.close()
    }
}

private suspend inline fun <T> Signal<T>.executeAndWaitSignal(callback: () -> Unit): T {
    val deferred = CompletableDeferred<T>()
    val closeable = this.once { deferred.complete(it) }
    try {
        callback()
        return deferred.await()
    } finally {
        closeable.close()
    }
}

private fun View.removeFromParent() {
    (parent as? ViewGroup?)?.removeView(this)
}

