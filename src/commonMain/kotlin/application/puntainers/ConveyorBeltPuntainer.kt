package application.puntainers

import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

/** This class is used in particular for the conveyor belt
 * it can read an arbitrary number of images from belt folder, and rotate them clockwise or counterclockwise based on the input
 */
class ConveyorBeltPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("conveyorBeltPuntainer",relativeRectangle) {
    private suspend fun init() {
        (1..3).forEach { i->
            visualList.add(punImage(relativeRectangle = oneRectangle(), bitmap =resourcesVfs["workshop/Convey_$i.png"].readBitmap()).also {
                it.visible = i==1
            })
        }

    }
    private var activeFrame = 0.0
    private val visualList = mutableListOf<Puntainer>()

    /** updates belt clockwise or counterclockise on every int increment
     * actual direction depends on the order of images (böyle şeyleri kimse bilemez)
     */
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