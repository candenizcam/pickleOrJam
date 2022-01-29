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
            return Rectangle(0.0, windowSize.width.toDouble(), 0.0, windowSize.height.toDouble())
        }

    val gameState = GameState(0)
    var inputList = mutableListOf<Fruit>()

    fun initInputs() {
        val anormalFlist = listOf(
            "Cat",
            "Game Jam",
            "Snowman",
            "Bubble Gum",
            "Donut",
            "Nail Polish",
            "Mug",
            "Notebook",
            "Rubber Duck",
            "Tent",
            "Headphones",
            "Watch",
            "Controller",
            "Spaghetti",
            "Plane",
            "Fridge",
            "Water Bottle",
            "Lamp",
            "CD",
            "Plate",
            "Car",
            "Pizza",
            "Table",
            "Mask",
            "Laptop",
            "Hamburger",
            "Socks",
            "Bowling Ball",
            "Charging Cable",
            "Atom"
        )
        val normalFlist = listOf(
            "Banana",
            "Strawberry",
            "Rose",
            "Apricot",
            "Apple",
            "Peach",
            "Lavender",
            "Grape",
            "Cherry",
            "Pumpkin",
            "Dandelion",
            "Pine Cone",
            "Cinnamon",
            "Ginger",
            "Lemon",
            "Tomato",
            "Carrot",
            "Eggplant",
            "Onion",
            "Red Pepper",
            "Egg",
            "Shrimp",
            "Tobasco",
            "Anchovy",
            "Garlic",
            "Broccoli",
            "Olive",
            "Red Cabbage",
            "Green Beans",
            "Cucumber"
        )
        val pNormalList = listOf(0, 0, 0, 0, 0, 4, 6, 6, 12, 13, 19, 30, 31, 33, 37, 44, 53, 59, 60, 60, 69, 71, 76, 77, 80, 80, 81, 87, 94, 94)
        val pAnormalList = listOf(100, 0, 6, 19, 19, 21, 28, 38, 38, 50, 52, 53, 54, 60, 62, 63, 63, 65, 65, 69, 69, 71, 76, 81, 81, 82, 88, 88, 94, 100)


        (0 until 7).forEach {
            normalFlist.indices.forEach {
                inputList.add(Fruit(normalFlist[it], pNormalList[it], 100 - pNormalList[it], true))
            }
        }

        (0 until 3).forEach {
            anormalFlist.indices.forEach {
                inputList.add(Fruit(anormalFlist[it], pAnormalList[it], 100 - pAnormalList[it], false))
            }
        }
    }


    //newInput
    //type
}