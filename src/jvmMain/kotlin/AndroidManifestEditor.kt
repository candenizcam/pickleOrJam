import com.soywiz.korge.plugin.KorgePluginExtension

class AndroidManifestEditor : KorgePluginExtension(){
    override fun getAndroidManifestApplication(): String? =
        """<activity android:exported="true"/>"""

    override fun getAndroidDependencies() =
        listOf("com.google.android.gms:play-services-ads:20.5.0")
}