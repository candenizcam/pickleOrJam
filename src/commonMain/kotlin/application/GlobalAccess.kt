package application

import com.soywiz.korio.file.std.resourcesVfs
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

    val gameState = GameState(0)
    var inputList = mutableListOf<Fruit>()

    var levels = mutableListOf<Level>()

    fun initLevels() {
        val fList = listOf("apple", "orange", "cucumber", "eggplant")
        val pList = listOf(100, 200, 300, 500)
        val jList = listOf(300, 500, 100, 200)
        val fruitList = mutableListOf<Fruit>()
        fList.indices.forEach {
            fruitList.add(Fruit(fList[it], pList[it], jList[it]))
        }

        levels.add(Level(fruitList, 120))
    }




    //newInput
    //type
}