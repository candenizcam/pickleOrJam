import com.soywiz.korge.gradle.*

buildscript {
	val korgePluginVersion: String by project

	repositories {
		mavenLocal()
		mavenCentral()
		google()
		maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
		maven { url = uri("https://plugins.gradle.org/m2/") }
		maven { url = uri("https://dl.bintray.com/soywiz/soywiz") }
		maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
		maven { url = uri("https://dl.bintray.com/kotlin/kotlin-dev") }
		maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
		maven { url = uri("https://dl.bintray.com/jetbrains/kotlin-native-dependencies") }
	}
	dependencies {
		classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
		classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.10")
		classpath("com.google.gms:google-services:4.3.10")
	}
}
apply<KorgeGradlePlugin>()
apply(plugin = "kotlinx-serialization")

korge {
	//admob("ca-app-pub-3705202925859468~3468110756")
	id = "com.pungo.pickleorjam"
	name = "pickleorjam"
	orientation = Orientation.LANDSCAPE
	icon = layout.projectDirectory.file("icon.png").asFile

	androidCompileSdk = 31
	androidTargetSdk = 30
	androidMinSdk = 20

	config("ADMOB_APP_ID", "ca-app-pub-3940256099942544~3347511713")

	supportSwf()
	androidPermission("android.permission.INTERNET")

	//targetJvm()
	//targetJs()
	//targetDesktop()
	//targetIos()
	targetAndroidIndirect()
	//targetAndroidDirect()
}

apply(plugin = "com.google.gms.google-services")
