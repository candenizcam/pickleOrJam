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

    var musicToggle = { state : Boolean -> }

    fun rectFromXD(corner: Vector, width: Int, height: Int, relativeRect: Rectangle?=null): Rectangle {
        return if(relativeRect!=null){
            virtualRect.fromRated(relativeRect)
        }else{
            virtualRect
        }.toRated(Rectangle(Vector(corner.x,virtualSize.height-corner.y), width.toDouble(), height.toDouble(), cornerType = Rectangle.Corners.TOP_LEFT))
    }

    var inputList = mutableListOf<Fruit>()

    var levels = mutableListOf<Level>()

    fun initLevels() {

        val fList = listOf("Apple", "Lemon", "Bowling_Ball", "Cherry", "Rubber_Duck", "Cucumber", "Eggplant", "orange")
        val pList = listOf(44, 37, 0, 38, 60, 94, 82, 12) //min 195

        val fList1 = listOf("Apricot", "Cherry", "Lemon", "Carrot", "Tomato", "Onion", "Broccoli", "Cucumber")
        val pList1 = listOf(0, 12, 37, 53, 44, 60, 80, 94) //min 199
        val fruitList1 = mutableListOf<Fruit>()
        fList1.indices.forEach {
            fruitList1.add(Fruit(fList1[it], pList1[it], 100-pList1[it]))
        }
        levels.add(Level(fruitList1,30,levels.size*50+300))

        val fList2 = listOf("Lavender", "Cherry", "Lemon", "Hamburger", "Dandelion", "Shrimp", "Garlic", "Egg")
        val pList2 = listOf(6, 12, 37, 82, 19, 71, 80, 69) //min 172
        val fruitList2 = mutableListOf<Fruit>()
        fList2.indices.forEach {
            fruitList2.add(Fruit(fList2[it], pList2[it], 100-pList2[it]))
        }
        levels.add(Level(fruitList2,30,levels.size*50+300))

        val fList3 = listOf("Peach", "Grape", "Donut", "Mug", "Tomato", "Onion", "Lamp", "Shrimp", "Pizza", "Olive")
        val pList3 = listOf(4, 6, 19, 28, 44, 60, 65, 71, 71, 81) //253
        val fruitList3 = mutableListOf<Fruit>()
        fList3.indices.forEach {
            fruitList3.add(Fruit(fList3[it], pList3[it], 100-pList3[it]))
        }
        levels.add(Level(fruitList3,30,levels.size*50+300))

        val fList4 = listOf("Snowman", "Grape", "Pumpkin", "Bubble_Gum", "Nail_Polish", "Pine_Cone", "Rubber_Duck", "Onion", "Plane", "Car", "Bowling_ball", "Atom")
        val pList4 = listOf(6, 6, 13, 19, 21, 30, 38, 60, 62, 69, 88, 100) //254
        val fruitList4 = mutableListOf<Fruit>()
        fList4.indices.forEach {
            fruitList4.add(Fruit(fList4[it], pList4[it], 100-pList4[it]))
        }
        levels.add(Level(fruitList4,30,levels.size*50+300))

        val fList5 = listOf("Banana", "Strawberry", "Rose", "Cat", "Apple", "Game_Jam", "Donut", "Cinnamon", "Ginger", "Headphones", "Watch", "Controller", "Eggplant", "Anchovy", "Broccoli", "Socks")
        val pList5 = listOf(0, 0, 0, 100, 0, 0, 19, 31, 33, 52, 53, 54, 59, 77, 80, 88)
        val fruitList5 = mutableListOf<Fruit>()
        fList5.indices.forEach {
            fruitList5.add(Fruit(fList5[it], pList5[it], 100-pList5[it]))
        }
        levels.add(Level(fruitList5,30,levels.size*50+300))

    }

    val fullFlist = listOf(
        "Cat",
        "Game_Jam",
        "Snowman",
        "Bubble_Gum",
        "Donut",
        "Nail_Polish",
        "Mug",
        "Notebook",
        "Rubber_Duck",
        "Headphones",
        "Watch",
        "Controller",
        "Plane",
        "Lamp",
        "Plate",
        "Car",
        "Pizza",
        "Hamburger",
        "Socks",
        "Bowling_Ball",
        "Atom",
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
        "Tobasco_Pepper",
        "Anchovy",
        "Garlic",
        "Broccoli",
        "Olive",
        "Cucumber"
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
        "Tobasco_Pepper",
        "Anchovy",
        "Garlic",
        "Broccoli",
        "Olive",
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
        94
    )
    val pFullList = listOf(
        100,
        0,
        6,
        19,
        19,
        21,
        28,
        38,
        38,
        52,
        53,
        54,
        62,
        63,
        69,
        69,
        71,
        82,
        88,
        88,
        100,
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
        94
    )
}