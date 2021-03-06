package application.puntainers

import application.GlobalAccess
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.vector.rect
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.uiElements.Button

class WorkshopPuntainer private constructor(relativeRectangle: Rectangle) :
    Puntainer("workshopPuntainer", relativeRectangle) {
    private val fruitList = mutableListOf<Basket>()
    var fruitPos = Vector(836.0 / GlobalAccess.windowSize.height, 0.5)
        private set
    val choicePos = Vector(0.5, 536.0 / GlobalAccess.windowSize.height)
    var onChoice: (String,Int)->Int = { foodId: String, choice: Int -> 0 } //choice 0-> pickle; choice 1-> jam
    var onNewFruit = {name: String->}
    var activeBasket: Basket? = null
        private set
    val fruitRectangle =
        Rectangle(Vector(0.0, 0.0), 200.0 / GlobalAccess.windowSize.width, 200.0 / GlobalAccess.windowSize.height)
    val allBasketsList = mutableListOf<Basket>()

    private suspend fun init() {
        resourcesVfs["workshop/background.png"].readBitmap().also {
            punImage("id", Rectangle(0.0, 1.0, 0.0, 1.0), bitmap = it)
            punImage("fruitBasket", Rectangle(1.25, 1.5, 0.25, 0.5), bitmap = it)
        }


        allBasketsList.addAll(
            GlobalAccess.fullFlist.mapIndexed { index, it->

                Basket(it, GlobalAccess.commonAssets.fruits.sliceWithSize(204*(index.mod(8)),204*(index/8),200,200)
                )
            }
        )



        val rectByPixel = GlobalAccess.virtualRect.fromRated(relativeRectangle)





        addPuntainer(
            ConveyorBeltPuntainer.create(
                Rectangle(
                    0.0,
                    1.0,
                    360.0 / rectByPixel.height,
                    620.0 / rectByPixel.height
                )
            )
        )

        resourcesVfs["workshop/Vinegar.png"].readBitmap().also {
            punImage(
                "vinegar",
                rectByPixel.toRated(Rectangle(Vector(64.0, 400.0), 240.0, 360.0, Rectangle.Corners.TOP_LEFT)),
                bitmap = it
            )
        }

        resourcesVfs["workshop/Sugar.png"].readBitmap().also {
            punImage(
                "sugar",
                rectByPixel.toRated(Rectangle(Vector(976.0, 400.0), 240.0, 360.0, Rectangle.Corners.TOP_LEFT)),
                bitmap = it
            )
        }











        val transparentBlock = Bitmap32(10, 10).context2d {
            this.rect(0, 0, 10, 10)
            this.fill(Colour.rgba(0.0, 0.0, 0.0, 0.0).korgeColor)
        }




        addPuntainer(
            JarPuntainer.create(
                rectByPixel.toRated(Rectangle(Vector(520.0, 700.0), 240.0, 280.0, Rectangle.Corners.TOP_LEFT))
            )
        )

        val b1 = Button(
            "pickleButton",
            Rectangle(0.0, 0.4, 0.0, 620.0 / rectByPixel.height),
            transparentBlock,
            resourcesVfs["workshop/vinegar_glow.png"].readBitmap(),
            hoverBitmap = resourcesVfs["workshop/vinegar_glow.png"].readBitmap()

        )

        val b2 = Button(
            "jamButton",
            Rectangle(0.6, 1.0, 0.0, 620.0 / rectByPixel.height),
            transparentBlock,
            resourcesVfs["workshop/sugar_glow.png"].readBitmap(),
            hoverBitmap = resourcesVfs["workshop/sugar_glow.png"].readBitmap(),
        )

        b1.clickFunction = {

            picklePressed()

        }
        b1.inactive = true
        b2.clickFunction = {

            jamPressed()

        }
        b2.inactive = true

        addPuntainer(b1)
        addPuntainer(b2)

        addPuntainer(
            punImage("vinBar",
                GlobalAccess.rectFromXD(Vector(64.0,636.0),240,24),
                resourcesVfs["UI/VinBar_inside.png"].readBitmap()
            )
        )

        addPuntainer(
            punImage("sugBar",
                GlobalAccess.rectFromXD(Vector(976.0,636.0),240,24),
                resourcesVfs["UI/SugBar_inside.png"].readBitmap()
            )
        )

        val cff = resourcesVfs["much_coin.png"].readBitmap()
        val sc = resourcesVfs["such_coin.png"].readBitmap()

        punImage("muchCoin",
            GlobalAccess.rectFromXD(Vector(640.0,516.0),144,144),
            cff
        ).also { it.visible=false }

        punImage("suchCoin",
            GlobalAccess.rectFromXD(Vector(640.0,536.0),144,144),
            sc
        ).also { it.visible=false }




    }

    fun coinVisible(n: Int){ // -1 nothing 0 such 1 much
        puntainers.first { it.id=="muchCoin" }.visible = n==1
        puntainers.first { it.id=="suchCoin" }.visible = n==0
    }

    fun updateSugarCount(n: Int){
        puntainers.first { it.id == "sugBar" }.resizeRect(GlobalAccess.rectFromXD(Vector(976.0,636.0),(240/10.0*n).toInt(),24))

    }

    fun updateVinCount(n: Int) {
        puntainers.first { it.id == "vinBar" }.resizeRect(GlobalAccess.rectFromXD(Vector(64.0,636.0),(240/10.0*n).toInt(),24))
    }




    fun picklePressed() {
        if (choicePos == fruitPos) {
            val remain = onChoice(activeBasket!!.id, 0)
            if(remain>=0){
                buttonsActive(false)
                activeBasket!!.status = 0
                ((puntainers.first { it.id == "jarPuntainer" }) as JarPuntainer).signVisible(0)
                updateVinCount(remain)
            }else{
                (puntainers.first { it.id=="pickleButton" } as Button).inactive = true
            }
        }

    }

    fun jamPressed() {
        if (choicePos == fruitPos) {
            val remain = onChoice(activeBasket!!.id, 1)
            if(remain>=0){
                buttonsActive(false)
                activeBasket!!.status = 1
                ((puntainers.first { it.id == "jarPuntainer" }) as JarPuntainer).signVisible(1)
                updateSugarCount(remain)
            }else{
                (puntainers.first { it.id=="jamButton" } as Button).inactive = true
            }


        }

    }

    fun updateClockBySec(sec: Int) {
        (puntainers.first { it.id == "clockPuntainer" } as ClockPuntainer).setTimeAsSeconds(sec)
    }

    suspend fun openLevel(foodToOpen: List<String>) {

        openConveyorFood(foodToOpen)
    }

    suspend fun openConveyorFood(l: List<String>) {
        fruitList.clear()




        fruitList.addAll(
            l.map { thisl ->
                allBasketsList.first { it.id== thisl}
            }
        )
    }

    fun deployNewFood() {
        puntainers.first { it.id == "fruitBasket" }.also {
            puntainers.remove(it)
            removeChild(it)
            activeBasket = fruitList.random().copy()
            onNewFruit(activeBasket!!.id)
            punImage(
                "fruitBasket",
                Rectangle(
                    Vector(0.5, 836.0 / GlobalAccess.windowSize.height),
                    fruitRectangle.width,
                    fruitRectangle.height
                ),
                activeBasket!!.bitmap
            )
        }
        val toUp = puntainers.first { it.id == "jarPuntainer" }
        removeChild(toUp)
        addChild(toUp)
        ((puntainers.first { it.id == "jarPuntainer" }) as JarPuntainer).signVisible(-2)
        fruitPos = Vector(0.5, 836.0 / GlobalAccess.windowSize.height)
    }


    /** This moves by step in relative fashion
     *
     */
    fun discreteMove(x: Int, y: Int, xStep: Double = 30.0, yStep: Double = 30.0) {
        val rectByPixel = GlobalAccess.virtualRect.fromRated(relativeRectangle)
        val verticalStep = xStep / rectByPixel.width
        val horizontalStep = yStep / rectByPixel.height

        if (x != 0) {
            (puntainers.first { it.id == "conveyorBeltPuntainer" } as ConveyorBeltPuntainer).update(x.toDouble())
        } else if (y != 0) {
            if (fruitPos.y - horizontalStep * 0.5 <= choicePos.y) {
                buttonsActive(true)
                fruitPos = Vector(fruitPos.x, choicePos.y)
                ((puntainers.first { it.id == "jarPuntainer" }) as JarPuntainer).signVisible(-1)
                return
            }
        } else {
            return
        }
        fruitPos = Vector(fruitPos.x + verticalStep * x, fruitPos.y + horizontalStep * y)

        puntainers.first { it.id == "jarPuntainer" }.also {
            it.resizeRect(
                Rectangle(
                    Vector(fruitPos.x, choicePos.y + 24.0 / rectByPixel.height),
                    it.relativeRectangle.width,
                    it.relativeRectangle.height
                )
            )
        }

        puntainers.first { it.id == "fruitBasket" }.also {
            it.resizeRect(Rectangle(fruitPos, it.relativeRectangle.width, it.relativeRectangle.height))
        }
    }

    fun buttonsActive(b: Boolean) {
        (puntainers.first { it.id == "jamButton" } as Button).inactive = !b
        (puntainers.first { it.id == "pickleButton" } as Button).inactive = !b
    }

    // alttakini tercihen kullanmay??n
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

    data class Basket(val id: String, val bitmap: BitmapSlice<Bitmap>, var status: Int = -1)


    companion object {
        suspend fun create(
            relativeRectangle: Rectangle
        ): WorkshopPuntainer {
            return WorkshopPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}