package application

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button
import pungine.uiElements.PunImage

class WorkshopPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("workshopPuntainer",relativeRectangle) {
    private val fruitList= mutableListOf<Basket>()
    var conveyorPos = Vector(1.2,0.5)
        private set
    private val choicePos = Vector(0.5,0.5)
    var onChoice = {foodId: String, choice: Int->} //choice 0-> pickle; choice 1-> jam

    var activeBasket: Basket? = null
    private set
    val fruitRectangle = Rectangle(Vector(0.0,0.0), 180.0/GlobalAccess.windowSize.width,180.0/GlobalAccess.windowSize.height)

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

        b1.clickFunction = {
            if(GlobalAccess.gameState.vinegar>0){
                picklePressed()
            }
        }
        b2.clickFunction = {
            if(GlobalAccess.gameState.sugar>0){
                jamPressed()
            }
        }

        addPuntainer(b1)
        addPuntainer(b2)


        addPuntainer(ConveyorBeltPuntainer.create(Rectangle(0.0,1.0,0.2,0.8)))

        addPuntainer(
            ClockPuntainer.create(
                GlobalAccess.virtualRect.toRated( Rectangle(Vector(1116.0,704.0),148.0,68.0, cornerType = Rectangle.Corners.TOP_LEFT )),
                Rectangle(0.0,148.0,0.0,68.0))
        )





    }

    fun picklePressed(){
        if(choicePos==conveyorPos){
            (puntainers.first { it.id == "fruitBasket" } as PunImage).colorMul = Colour.GREEN.korgeColor
            activeBasket!!.status = 0
            onChoice(activeBasket!!.id,activeBasket!!.status)
        }

    }

    fun jamPressed(){
        if(choicePos==conveyorPos){
            (puntainers.first { it.id == "fruitBasket" } as PunImage).colorMul = Colour.RED.korgeColor
            activeBasket!!.status = 1
            onChoice(activeBasket!!.id,activeBasket!!.status)
        }

    }

    fun updateClockBySec(sec: Int){

        (puntainers.first { it.id == "clockPuntainer" } as ClockPuntainer).setTimeAsSeconds(sec.toInt())
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
            activeBasket = fruitList.random().copy()
            punImage("fruitBasket",Rectangle(Vector(0.5,1.1),fruitRectangle.width,fruitRectangle.height),activeBasket!!.bitmap)
        }
        conveyorPos= Vector(0.5,1.1)


    }

    fun moveOnConveyor(setX: Double=conveyorPos.x, setY: Double=conveyorPos.y){
        if(setX!=conveyorPos.x){
            (puntainers.first { it.id == "conveyorBeltPuntainer" } as ConveyorBeltPuntainer).update( (setX-conveyorPos.x)*10.0)
        }
        conveyorPos = Vector(setX,setY)
        puntainers.first { it.id == "fruitBasket" }.also {
            it.resizeRect(Rectangle(conveyorPos,it.relativeRectangle.width,it.relativeRectangle.height))
        }



    }

    data class Basket(val id: String, val bitmap: Bitmap, var status: Int = -1)


    companion object {
        suspend fun create(relativeRectangle: Rectangle
        ): WorkshopPuntainer {
            return WorkshopPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}