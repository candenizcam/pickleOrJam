package application

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.context2d
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.vector.rect
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.uiElements.Button
import pungine.uiElements.PunImage

class WorkshopPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("workshopPuntainer",relativeRectangle) {
    private val fruitList= mutableListOf<Basket>()
    var fruitPos = Vector(836.0/GlobalAccess.windowSize.height,0.5)
        private set
    val choicePos = Vector(0.5,536.0/GlobalAccess.windowSize.height)
    var onChoice = {foodId: String, choice: Int->} //choice 0-> pickle; choice 1-> jam
    var activeBasket: Basket? = null
    private set
    val fruitRectangle = Rectangle(Vector(0.0,0.0), 200.0/GlobalAccess.windowSize.width,200.0/GlobalAccess.windowSize.height)

    private suspend fun init() {
        resourcesVfs["grid44.png"].readBitmap().also {
            punImage("id", Rectangle(0.0,1.0,0.0,1.0), bitmap = it)
            punImage("fruitBasket", Rectangle(1.25,1.5,0.25,0.5), bitmap = it)
        }

        resourcesVfs["game_logo.png"].readBitmap().also {
            punImage("logo", Rectangle(0.5-240.0/GlobalAccess.windowSize.width,0.5+240.0/GlobalAccess.windowSize.width,1.0-310.0/GlobalAccess.windowSize.height,1.0), bitmap = it)
        }

        val rectByPixel = GlobalAccess.virtualRect.fromRated(relativeRectangle)

        val transparentBlock = Bitmap32(10,10).context2d {
            this.rect(0,0,10,10)
            this.fill(Colour.rgba(0.0,0.0,0.0,0.0).korgeColor)
        }



        addPuntainer(ConveyorBeltPuntainer.create(Rectangle(0.0,1.0,360.0/rectByPixel.height,620.0/rectByPixel.height)))

        resourcesVfs["workshop/Vinegar.png"].readBitmap().also {
            punImage("vinegar", rectByPixel.toRated(Rectangle(Vector(64.0,400.0),240.0,360.0,Rectangle.Corners.TOP_LEFT)), bitmap = it)
        }

        resourcesVfs["workshop/Sugar.png"].readBitmap().also {
            punImage("sugar", rectByPixel.toRated(Rectangle(Vector(976.0,400.0),240.0,360.0,Rectangle.Corners.TOP_LEFT)), bitmap = it)
        }













        addPuntainer(JarPuntainer.create(
            rectByPixel.toRated(Rectangle(Vector(520.0,700.0),240.0,280.0,Rectangle.Corners.TOP_LEFT)))
        )

        val b1 = Button("pickleButton",Rectangle(0.0,0.4,0.0,620.0/rectByPixel.height),transparentBlock,transparentBlock,hoverBitmap = resourcesVfs["buttons/pickle_jar.png"].readBitmap())

        val b2 = Button("jamButton",Rectangle(0.6,1.0,0.0,620.0/rectByPixel.height),transparentBlock,transparentBlock,hoverBitmap = resourcesVfs["buttons/jam_jar.png"].readBitmap())

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



    }

    fun picklePressed(){
        if(choicePos==fruitPos){
            buttonsActive(false)
            (puntainers.first { it.id == "fruitBasket" } as PunImage).colorMul = Colour.GREEN.korgeColor
            activeBasket!!.status = 0
            onChoice(activeBasket!!.id,activeBasket!!.status)
        }

    }

    fun jamPressed(){
        if(choicePos==fruitPos){
            buttonsActive(false)
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
            punImage("fruitBasket",Rectangle(Vector(0.5,836.0/GlobalAccess.windowSize.height),fruitRectangle.width,fruitRectangle.height),activeBasket!!.bitmap)
        }
        val toUp = puntainers.first { it.id=="jarPuntainer" }
        removeChild(toUp)
        addChild(toUp)
        fruitPos= Vector(0.5,836.0/GlobalAccess.windowSize.height)
    }




    /** This moves by step in relative fashion
     *
     */
    fun discreteMove(x: Int, y: Int, xStep: Double = 30.0, yStep: Double=30.0){
        val rectByPixel = GlobalAccess.virtualRect.fromRated(relativeRectangle)
        val verticalStep = xStep/rectByPixel.width
        val horizontalStep = yStep/rectByPixel.height

        if(x!=0){
            (puntainers.first { it.id == "conveyorBeltPuntainer" } as ConveyorBeltPuntainer).update( x.toDouble())
        }else if(y!=0){
            if(fruitPos.y-horizontalStep*0.5<= choicePos.y){
                buttonsActive(true)
                fruitPos = Vector(fruitPos.x,choicePos.y)
                return
            }
        }else{
            return
        }
        fruitPos = Vector(fruitPos.x + verticalStep*x,fruitPos.y + horizontalStep*y)

        puntainers.first { it.id == "jarPuntainer" }.also {
            it.resizeRect(Rectangle(Vector(fruitPos.x,choicePos.y+24.0/rectByPixel.height),it.relativeRectangle.width,it.relativeRectangle.height))
        }

        puntainers.first { it.id == "fruitBasket" }.also {
            it.resizeRect(Rectangle(fruitPos,it.relativeRectangle.width,it.relativeRectangle.height))
        }
    }

    fun buttonsActive(b: Boolean){
        (puntainers.first { it.id=="jamButton" } as Button).inactive = !b
        (puntainers.first { it.id=="pickleButton" } as Button).inactive = !b
    }

    // alttakini tercihen kullanmayın
    /*
    fun moveOnConveyor(setX: Double=fruitPos.x, setY: Double=fruitPos.y){
        if(setX!=fruitPos.x){
            //(puntainers.first { it.id == "conveyorBeltPuntainer" } as ConveyorBeltPuntainer).update( (setX-jarPos.x)*10.0)
        }
        conveyorHolder+= (setX-fruitPos.x)*1000
        println(conveyorHolder)
        if(conveyorHolder>30.0){
            (puntainers.first { it.id == "conveyorBeltPuntainer" } as ConveyorBeltPuntainer).update( 1.0)
            conveyorHolder = conveyorHolder.rem(30.0)
        }else if (conveyorHolder< -30.0){
            (puntainers.first { it.id == "conveyorBeltPuntainer" } as ConveyorBeltPuntainer).update( -1.0)
            conveyorHolder = conveyorHolder.rem(-30.0)
        }

        fruitPos = Vector(setX,setY)
        puntainers.first { it.id == "fruitBasket" }.also {
            it.resizeRect(Rectangle(fruitPos,it.relativeRectangle.width,it.relativeRectangle.height))
        }
    }

     */

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