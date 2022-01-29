package application

import com.soywiz.kds.iterators.fastForEachReverse
import com.soywiz.korge.input.onUp
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

class MoneyPuntainer private constructor(relativeRectangle: Rectangle, pixelSize: Rectangle): Puntainer("moneyPuntainer",relativeRectangle)  {
    init {
    }
    val pixelSize = pixelSize
    val clockRectList = mutableListOf<Rectangle>()
    val digitNo = 7

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
            (0 until digitNo).map {
                Rectangle(Vector(0.5,0.5),wNumber,hNumber).fromRated(Rectangle(it/digitNo.toDouble(),(it+1)/digitNo.toDouble(),0.0,1.0))
            }
        )
        solidRect("bg", oneRectangle(), colour = Colour.CYAN)

        //punImage("test", oneRectangle(),slices[0])

        //punImage("id",oneRectangle(),resourcesVfs["number_sheet.png"].readBitmap())

        clockRectList.forEachIndexed { index, rectangle ->
            if(index==digitNo-1){
                punImage("pundollarSign",clockRectList[index],slices[0])
            }else{
                punImage("digit_$index",clockRectList[index],slices[0])
            }

        }


        //punImage("digit_2",clockRectList[1],slices[0])
        //punImage("digit_3",clockRectList[3],slices[0])
        //punImage("digit_4",clockRectList[4],slices[0])

        //punImage("birVarmisBirYokmus",clockRectList[2], slices[10] )

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




    fun setMoney(v: Int){
        val vs = v.toString().padStart(6,'0')

        puntainers.filter { it.id!!.contains("digit") }.forEach {
            puntainers.remove(it)
            removeChild(it)
        }


        clockRectList.forEachIndexed { index, rectangle ->
            if(index==digitNo-1){
                //punImage("pundollarSign",clockRectList[0],slices[0])
            }else{
                val ind = if(vs.length>index){
                    vs[index].toString().toInt()
                }else{
                    0
                }
                punImage("digit_$index",rectangle,sliceList[ind])
            }

        }

    }


    companion object {
        suspend fun create(relativeRectangle: Rectangle, pixelSize: Rectangle
        ): MoneyPuntainer {
            return MoneyPuntainer(relativeRectangle, pixelSize).also {
                it.init()
            }
        }
    }
}