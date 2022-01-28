package application

import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import pungine.Puntainer
import pungine.geometry2D.Rectangle

class ConveyorBeltPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("conveyorBeltPuntainer",relativeRectangle) {
    private suspend fun init() {
        (1..4).forEach { i->
            visualList.add(punImage(relativeRectangle = relativeRectangle, bitmap =resourcesVfs["belt/belt_$i.png"].readBitmap()).also {
                it.visible = i==1
            })
        }

    }
    private var activeFrame = 0.0
    private val visualList = mutableListOf<Puntainer>()

    fun update(n: Double=0.0){
        activeFrame = (activeFrame+n).rem(visualList.size.toDouble())
        if(activeFrame<0) activeFrame += visualList.size
        visualList.forEachIndexed{ index,it->
            it.visible = index==activeFrame.toInt()
        }
    }


    companion object {
        suspend fun create(relativeRectangle: Rectangle
        ): ConveyorBeltPuntainer {
            return ConveyorBeltPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}