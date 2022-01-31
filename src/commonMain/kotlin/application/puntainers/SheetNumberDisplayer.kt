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

class SheetNumberDisplayer private constructor(id: String? =null, relativeRectangle: Rectangle, pixelSize: Rectangle, val digitNo: Int, val bg: Boolean=true, val moneySign: Boolean=false, val minusSign: Boolean=false): Puntainer(id,relativeRectangle)  {
    init {
    }
    val pixelSize = pixelSize
    val colRectList = mutableListOf<Rectangle>()
    val activeDigitNo: Int
    get() {
        return digitNo+ if(moneySign) 1 else 0 + if(minusSign) 1 else 0
    }

        private suspend fun init() {
        val sheet = resourcesVfs["number_sheet.png"].readBitmap()
        val h = sheet.height
        val w = sheet.width/11

        val slices = (0 until 11).map {base-> sheet.sliceWithBounds(base*w,0,(base+1)*w,h) }
        sliceList.addAll(slices)



        val wNumber = w*activeDigitNo/pixelSize.width
        val hNumber = h/pixelSize.height

        colRectList.addAll(
            (0 until activeDigitNo).map {
                Rectangle(Vector(0.5,0.5),wNumber,hNumber).fromRated(Rectangle(it/activeDigitNo.toDouble(),(it+1)/activeDigitNo.toDouble(),0.0,1.0))
            }
        )
        if(bg){
            solidRect("bg", oneRectangle(), colour = Colour.CYAN)
        }


        //punImage("test", oneRectangle(),slices[0])

        //punImage("id",oneRectangle(),resourcesVfs["number_sheet.png"].readBitmap())

        colRectList.forEachIndexed { index, rectangle ->
            if(moneySign && index==colRectList.size-1){
                punImage("pundollarSign",rectangle,resourcesVfs["Pundollar.png"].readBitmap())
            }else if(minusSign){
                punImage("minusSign",rectangle,resourcesVfs["tire.png"].readBitmap())
            }else{
                punImage("digit_$index",colRectList[index],slices[0])
            }


        }
    }
    val sliceList = mutableListOf<BitmapSlice<Bitmap>>()




    fun setValue(v: Int){
        val vs = v.toString().padStart(digitNo,'0')

        puntainers.filter { it.id!!.contains("digit") }.forEach {
            puntainers.remove(it)
            removeChild(it)
        }


        colRectList.forEachIndexed { index, rectangle ->
            if(moneySign &&(index==colRectList.size-1)){
            }else if (minusSign&& (index==0)){
                puntainers.first { it.id == "minusSign" }.visible = v<0
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
        suspend fun create(id: String?=null, relativeRectangle: Rectangle, pixelSize: Rectangle, digitNo: Int,  bg: Boolean=true, moneySign: Boolean=false, minusSign: Boolean=false,
        ): SheetNumberDisplayer {
            return SheetNumberDisplayer(id,relativeRectangle, pixelSize,digitNo, bg, moneySign, minusSign).also {
                it.init()
            }
        }
    }
}