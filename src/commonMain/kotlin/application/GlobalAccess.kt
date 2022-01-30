package application

import com.soywiz.korma.geom.SizeInt
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector

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


    fun rectFromXD(corner: Vector, width: Int, height: Int, relativeRect: Rectangle?=null): Rectangle {
        return if(relativeRect!=null){
            virtualRect.fromRated(relativeRect)
        }else{
            virtualRect
        }.toRated(Rectangle(Vector(corner.x,virtualSize.height-corner.y), width.toDouble(), height.toDouble(), cornerType = Rectangle.Corners.TOP_LEFT))
    }

    val gameState = GameState(level= 0, money= 0)
    var inputList = mutableListOf<Fruit>()

    var levels = mutableListOf<Level>()

    fun initLevels() {
        val fList = listOf("Apple", "Lemon", "Bowling_Ball", "Cherry", "Rubber_Duck", "Cucumber", "Eggplant", "orange")
        val pList = listOf(44, 37, 0, 38, 60, 94, 82, 12) //min 195
        val fList1 = listOf("Apricot", "Cherry", "Lemon", "Carrot", "Tomato", "Onion", "Red_Cabbage", "Cucumber")
        val pList1 = listOf(0, 12, 37, 53, 44, 60, 87, 94) //min 199
        val fList2 = listOf("Lavender", "Cherry", "Lemon", "Hamburger", "Dandelion", "Shrimp", "Garlic", "Egg")
        val pList2 = listOf(6, 12, 37, 82, 19, 71, 80, 69) //min 172
        val fList3 = listOf("Peach", "Grape", "Donut", "Mug", "Tomato", "Onion", "Lamp", "Shrimp", "Pizza", "Olive")
        val pList3 = listOf(4, 6, 19, 28, 44, 60, 65, 71, 71, 81) //253
        val fList4 = listOf("Snowman", "Grape", "Pumpkin", "Bubble_Gum", "Nail_Polish", "Pine_Cone", "Rubber_Duck", "Onion", "Fridge", "Car", "Bowling_ball", "Green_Beans")
        val pList4 = listOf(6, 6, 13, 19, 21, 30, 38, 60, 63, 69, 88, 94) //259
        val fList5 = listOf("Peach", "Grape", "Donut", "Mug", "Tomato", "Onion", "Lamp", "Shrimp", "Pizza", "Olive")
        val pList5 = listOf(4, 6, 19, 28, 44, 60, 65, 71, 71, 81)
        val fruitList = mutableListOf<Fruit>()
        fList.indices.forEach {
            fruitList.add(Fruit(fList[it], pList[it], 100-pList[it]))
        }

        levels.add(Level(fruitList, 30, 300))
    }

    val anormalFlist = listOf(
        "Cat",
        "Game_Jam",
        "Snowman",
        "Bubble_Gum",
        "Donut",
        "Nail_Polish",
        "Mug",
        "Notebook",
        "Rubber_Duck",
        "Tent",
        "Headphones",
        "Watch",
        "Controller",
        "Spaghetti",
        "Plane",
        "Fridge",
        "Water_Bottle",
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
        "Bowling_Ball",
        "Charging_Cable",
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
        "Pine_Cone",
        "Cinnamon",
        "Ginger",
        "Lemon",
        "Tomato",
        "Carrot",
        "Eggplant",
        "Onion",
        "Red_Pepper",
        "Egg",
        "Shrimp",
        "Tobasco",
        "Anchovy",
        "Garlic",
        "Broccoli",
        "Olive",
        "Red_Cabbage",
        "Green_Beans",
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