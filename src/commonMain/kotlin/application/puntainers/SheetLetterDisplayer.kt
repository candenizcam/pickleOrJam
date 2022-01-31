package application.puntainers

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korim.bitmap.sliceWithBounds
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.dynamic.KDynamic.Companion.toInt
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle

class SheetLetterDisplayer private constructor(id: String? =null, relativeRectangle: Rectangle, pixelSize: Rectangle, val digitNo: Int, val bg: Boolean=true): Puntainer(id,relativeRectangle)  {
    init {
    }
    val pixelSize = pixelSize
    val colRectList = mutableListOf<Rectangle>()
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    private suspend fun init() {
        val sheet = resourcesVfs["FontHsrf.png"].readBitmap()
        val h = sheet.height

        val w = sheet.width/chars.length

        val slices = (0 until chars.length).map {base-> sheet.sliceWithBounds(base*w,0,(base+1)*w,h) }
        sliceList.addAll(slices)



        val wNumber = w*digitNo/pixelSize.width
        val hNumber = h/pixelSize.height

        colRectList.addAll(
            (0 until digitNo).map {
                Rectangle(Vector(0.5,0.5),wNumber,hNumber).fromRated(Rectangle(it/digitNo.toDouble(),(it+1)/digitNo.toDouble(),0.0,1.0))
            }
        )
        if(bg){
            solidRect("bg", oneRectangle(), colour = Colour.CYAN)
        }


        colRectList.forEachIndexed { index, rectangle ->
            punImage("digit_$index",colRectList[index],slices[0])


        }
    }
    val sliceList = mutableListOf<BitmapSlice<Bitmap>>()




    fun setValue(s: String){
        val vs = s.uppercase().padStart(digitNo,' ')
        puntainers.filter { it.id!!.contains("digit") }.forEach {
            puntainers.remove(it)
            removeChild(it)
        }


        colRectList.forEachIndexed { index, rectangle ->
            if(chars.contains(vs[index])){
                val ind = chars.indexOf( vs[index] )
                punImage("digit_$index",rectangle,sliceList[ind])
            }
        }

    }


    companion object {
        suspend fun create(id: String?=null, relativeRectangle: Rectangle, pixelSize: Rectangle, digitNo: Int, bg: Boolean=true
        ): SheetLetterDisplayer {
            return SheetLetterDisplayer(id,relativeRectangle, pixelSize,digitNo, bg).also {
                it.init()
            }
        }
    }
}