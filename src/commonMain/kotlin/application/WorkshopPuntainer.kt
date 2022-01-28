package application

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.uiElements.Button
import pungine.uiElements.PunImage

class WorkshopPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("workshopPuntainer",relativeRectangle) {
    private val fruitList= mutableListOf<Basket>()
    var conveyorPos: Double = 1.2
        private set
    var jarChosen: Boolean = false
        private set
    private val choicePos = 0.5
    var onChoice = {foodId: String, choice: Int->} //choice 0-> pickle; choice 1-> jam
    private var activeFood = ""
    private suspend fun init() {
        resourcesVfs["grid44.png"].readBitmap().also {
            punImage("id", Rectangle(0.0,1.0,0.0,1.0), bitmap = it)
            punImage("fruitBasket", Rectangle(1.25,1.5,0.25,0.5), bitmap = it)
        }

        resourcesVfs["game_logo.png"].readBitmap().also {
            punImage("logo", Rectangle(0.5-240.0/GlobalAccess.windowSize.width,0.5+240.0/GlobalAccess.windowSize.width,1.0-310.0/GlobalAccess.windowSize.height,1.0), bitmap = it)
        }

        val b1 = Button("pickleButton",Rectangle(0.1,0.3,0.1,0.3),resourcesVfs["buttons/pickle_jar.png"].readBitmap())

        val b2 = Button("jamButton",Rectangle(0.7,0.9,0.1,0.3),resourcesVfs["buttons/jam_jar.png"].readBitmap())
        b1.clickFunction = {picklePressed()}
        b2.clickFunction = {jamPressed()}

        addPuntainer(b1)
        addPuntainer(b2)
    }

    fun picklePressed(){
        if(choicePos==conveyorPos){
            jarChosen = true
            (puntainers.first { it.id == "fruitBasket" } as PunImage).colorMul = Colour.GREEN.korgeColor
            onChoice(activeFood,0)
        }

    }

    fun jamPressed(){
        if(choicePos==conveyorPos){
            jarChosen=true
            (puntainers.first { it.id == "fruitBasket" } as PunImage).colorMul = Colour.RED.korgeColor
            onChoice(activeFood,1)
        }

    }


    suspend fun openLevel(foodToOpen: List<String>){

        openConveyorFood(foodToOpen)
    }

    suspend fun openConveyorFood(l: List<String>){
        fruitList.clear()
        fruitList.addAll(l.map { Basket(it, resourcesVfs["fruits/$it.png"].readBitmap()) })
    }

    fun deployNewFood(){
        puntainers.first { it.id == "fruitBasket" }.also {
            puntainers.remove(it)
            removeChild(it)
            val chosenBasket = fruitList.random()
            punImage("fruitBasket",Rectangle(-180.0/GlobalAccess.windowSize.width,0.0,0.5,0.5+180.0/GlobalAccess.windowSize.height),chosenBasket.bitmap)
            activeFood = chosenBasket.id
        }
        conveyorPos=-0.1
        jarChosen = false


    }

    fun moveOnConveyor(setX: Double){
        conveyorPos = setX
        puntainers.first { it.id == "fruitBasket" }.also {
            it.resizeRect(Rectangle(Vector(conveyorPos,it.relativeRectangle.centre.y),it.relativeRectangle.width,it.relativeRectangle.height))
        }

    }

    data class Basket(val id: String, val bitmap: Bitmap)


    companion object {
        suspend fun create(relativeRectangle: Rectangle
        ): WorkshopPuntainer {
            return WorkshopPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}