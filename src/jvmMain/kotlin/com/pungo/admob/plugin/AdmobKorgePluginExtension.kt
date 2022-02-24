package com.pungo.admob.plugin

import com.soywiz.korge.plugin.KorgePluginExtension
import com.soywiz.korio.lang.quoted

class AdmobKorgePluginExtension : KorgePluginExtension(
	AdmobKorgePluginExtension::ADMOB_APP_ID
) {
	lateinit var ADMOB_APP_ID: String

	override fun getAndroidInit(): String? =
		"""try { 
			|android.util.Log.d("ADMOB", "Initializing...")
			|com.google.android.gms.ads.MobileAds.initialize(com.soywiz.korio.android.androidContext(), com.google.android.gms.ads.initialization.OnInitializationCompleteListener(){}) 
			|android.util.Log.d("ADMOB", "Initialized!") } catch (e: Throwable) { e.printStackTrace() }""".trimMargin()

	override fun getAndroidManifestApplication(): String? =
		"""<meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" android:value=${ADMOB_APP_ID.quoted} />"""

	override fun getAndroidDependencies() =
		listOf("com.google.android.gms:play-services-ads:20.5.0",
			"com.google.firebase:firebase-bom:29.1.0",
			"com.google.firebase:firebase-analytics-ktx:20.1.0"
		)
}
