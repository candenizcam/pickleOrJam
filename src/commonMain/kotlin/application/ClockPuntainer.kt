package application

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korim.bitmap.sliceWithBounds
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle

class ClockPuntainer private constructor(relativeRectangle: Rectangle, pixelSize: Rectangle): Puntainer("clockPuntainer",relativeRectangle)  {
    init {
    }
    val pixelSize = pixelSize
    val clockRectList = mutableListOf<Rectangle>()
    var setTime = 0

    private suspend fun init() {
        val sheet = resourcesVfs["number_sheet.png"].readBitmap()
        val h = sheet.height
        val w = sheet.width/11

        val slices = (0 until 11).map {base-> sheet.sliceWithBounds(base*w,0,(base+1)*w,h) }
        sliceList.addAll(slices)

        pixelSize.toRated(Rectangle(0.0,w*5.0,0.0,h.toDouble()))

        val wNumber = w*5/pixelSize.width
        val hNumber = h/pixelSize.height

        clockRectList.addAll(
            (0 until 5).map {
                Rectangle(Vector(0.5,0.5),wNumber,hNumber).fromRated(Rectangle(it/5.0,(it+1)/5.0,0.0,1.0))
            }
        )


        //punImage("test", oneRectangle(),slices[0])

        //punImage("id",oneRectangle(),resourcesVfs["number_sheet.png"].readBitmap())
        solidRect("bg", oneRectangle(), colour = Colour.YELLOW)

        punImage("digit_1",clockRectList[0],slices[0])
        punImage("digit_2",clockRectList[1],slices[0])
        punImage("digit_3",clockRectList[3],slices[0])
        punImage("digit_4",clockRectList[4],slices[0])

        punImage("birVarmisBirYokmus",clockRectList[2], slices[10] )

        /*
        oneRectangle().split(cols=listOf(0.23,0.23,0.08,0.23,0.23)).also {
            for (i in 0..4){
                if(i==2){
                    punImage("dots",it[1,3],slices[0])
                }else{
                    punImage("digit_$i",it[1,i+1],slices[0])
                }
            }
        }

         */



    }
    val sliceList = mutableListOf<BitmapSlice<Bitmap>>()


    fun updateClockDisplayer(){
        val t = setTime
        val min = t/60
        val sec = t%60
        val s1 = sec%10
        val s2 = sec/10
        val m1 = min%10
        val m2 = min/10

        puntainers.filter { it.id!!.contains("digit") }.forEach {
            puntainers.remove(it)
            removeChild(it)
        }

        punImage("digit_1",clockRectList[0],sliceList[m2])
        punImage("digit_2",clockRectList[1],sliceList[m1])
        punImage("digit_3",clockRectList[3],sliceList[s2])
        punImage("digit_4",clockRectList[4],sliceList[s1])

        //puntainers.first { it.id == "birVarmisBirYokmus" }.also {
        //    it.visible = !it.visible
        //}
    }


    fun setTimeAsSeconds(t: Int){
        setTime = t
    }


    companion object {
        suspend fun create(relativeRectangle: Rectangle, pixelSize: Rectangle
        ): ClockPuntainer {
            return ClockPuntainer(relativeRectangle, pixelSize).also {
                it.init()
            }
        }
    }
}