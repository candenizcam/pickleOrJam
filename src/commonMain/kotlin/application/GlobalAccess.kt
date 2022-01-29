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
            return Rectangle(0.0, windowSize.width.toDouble(), 0.0, windowSize.height.toDouble())
        }

    val gameState = GameState(level= 0, money= 0)
    var inputList = mutableListOf<Fruit>()

    var levels = mutableListOf<Level>()

    fun initLevels() {
        val fList = listOf("apple", "lemon", "bowling", "cherry", "duck", "cucumber", "eggplant", "orange")
        val pList = listOf(44, 37, 0, 38, 60, 94, 82, 12)
        val fList1 = listOf("Tomato", "Lemon", "Apricot", "Rubber Duck", "Onion", "Cucumber", "Hamburger", "Cherry")
        val pList1 = listOf(44, 37, 0, 38, 60, 94, 82, 12)
        val fList2 = listOf("Tomato", "Lemon", "Apricot", "Rubber Duck", "Onion", "Cucumber", "Hamburger", "Cherry")
        val pList2 = listOf(44, 37, 0, 38, 60, 94, 82, 12)
        val fruitList = mutableListOf<Fruit>()
        fList.indices.forEach {
            fruitList.add(Fruit(fList[it], pList[it], 100-pList[it]))
        }

        levels.add(Level(fruitList, 120))
    }

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
    val pNormalList = listOf(
        0,
        0,
        0,
        0,
        0,
        4,
        6,
        6,
        12,
        13,
        19,
        30,
        31,
        33,
        37,
        44,
        53,
        59,
        60,
        60,
        69,
        71,
        76,
        77,
        80,
        80,
        81,
        87,
        94,
        94
    )
    val pAnormalList = listOf(
        100,
        0,
        6,
        19,
        19,
        21,
        28,
        38,
        38,
        50,
        52,
        53,
        54,
        60,
        62,
        63,
        63,
        65,
        65,
        69,
        69,
        71,
        76,
        81,
        81,
        82,
        88,
        88,
        94,
        100
    )

    //newInput
    //type
}