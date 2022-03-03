package application

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

class CommonAssets {
    lateinit var largeButtons: Bitmap
    lateinit var mediumButtons: Bitmap
    lateinit var smallButtons: Bitmap
    lateinit var miscButtons: Bitmap
    lateinit var fruits: Bitmap

    suspend fun load(){
        largeButtons =  resourcesVfs["UI/Large_Buttons_Sprite.png"].readBitmap()
        mediumButtons =  resourcesVfs["UI/Medium_Buttons_Sprite.png"].readBitmap()
        smallButtons =  resourcesVfs["UI/Thin_Buttons_Sprite.png"].readBitmap()
        miscButtons =  resourcesVfs["UI/Misc_Buttons_Sprite.png"].readBitmap()
        fruits = resourcesVfs["fruits/Items_Sprite.png"].readBitmap()
    }
}