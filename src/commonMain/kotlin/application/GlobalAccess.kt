package application

import com.soywiz.korma.geom.SizeInt
import pungine.geometry2D.Rectangle

/** This file contains variables for global access in the application,
 * user can manipulate here during development
 */
object GlobalAccess {
    val virtualSize = SizeInt(1280, 720) // Virtual Size, this is the size that code pretends is the size of the screen
    var windowSize = SizeInt(1280, 720) // Window Size, this is the actual size of the window in pixels
    val virtualRect: Rectangle
    get() {
        return Rectangle(0.0, windowSize.width.toDouble(),0.0, windowSize.height.toDouble())
    }
}