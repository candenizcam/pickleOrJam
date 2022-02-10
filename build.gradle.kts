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
	}
}

apply<KorgeGradlePlugin>()
apply(plugin = "kotlinx-serialization")

korge {
	id = "com.pungo.pickleorjam"
	name = "pickleorjam"
	orientation = Orientation.LANDSCAPE

	supportSwf()

	targetJvm()
	targetJs()
	targetDesktop()
	targetIos()
	targetAndroidIndirect()
	targetAndroidDirect()
}
