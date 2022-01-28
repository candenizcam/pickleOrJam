package pungine

import application.GlobalAccess
import pungine.geometry2D.Rectangle
import com.soywiz.korma.geom.SizeInt
import pungine.geometry2D.Vector

/** This is the internal global access for variables that are used in pungine globally
 * right now it depends on the GlobalAccess file which while a part of pungine is not in the box
 * this can be further improved
 */
object InternalGlobalAccess {
    val virtualSize: SizeInt   // Virtual Size
    get() {
        return GlobalAccess.virtualSize
    }
    val windowSize: SizeInt
    get() {
        return GlobalAccess.windowSize
    }
    val virtualRect: Rectangle
    get() {
        return GlobalAccess.virtualRect
    }
}