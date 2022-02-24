package application.puntainers

import application.GlobalAccess
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.uiElements.Button

class PurchaseButtonsPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("pauseMenuPuntainer",relativeRectangle){
    var buySugClick = {}
    var buyVinClick = {}

    private suspend fun init() {
        Button("buySug", Rectangle(0.0,1.0,0.5,1.0),
            resourcesVfs["UI/buy_normal.png"].readBitmap(),
            resourcesVfs["UI/buy_pushed.png"].readBitmap(),
            resourcesVfs["UI/buy_hover.png"].readBitmap(),
        ).also {
            addPuntainer(it)

            it.clickFunction = {
                buySugClick()

            }

        }

        addPuntainer(
            SheetNumberDisplayer.create( "sugPrice",
                Rectangle(24.0/128.0, 1-24.0/128.0, (72.0+18.0)/72.0/2.0,(2*72.0-18.0)/72.0/2.0 ),
                Rectangle(0.0, 80.0, 0.0, 36.0),
                2,
                false,
                moneySign = true

            ).also {
                it.setValue(20)
            }
        )

        Button("buyVin", Rectangle(0.0,1.0,0.0,0.5),
            resourcesVfs["UI/buy_normal.png"].readBitmap(),
            resourcesVfs["UI/buy_pushed.png"].readBitmap(),
            resourcesVfs["UI/buy_hover.png"].readBitmap(),
        ).also {
            addPuntainer(it)
            it.clickFunction = {
                buyVinClick()
            }
        }

        addPuntainer(
            SheetNumberDisplayer.create( "vinPrice",
                Rectangle(24.0/128.0, 1-24.0/128.0, (18.0)/72.0/2.0,(72.0-18.0)/72.0/2.0 ),
                Rectangle(0.0, 80.0, 0.0, 36.0),
                2,
                false,
                moneySign = true
            ).also {
                it.setValue(20)
            }
        )
    }

    companion object {
        suspend fun create(relativeRectangle: Rectangle
        ): PurchaseButtonsPuntainer {
            return PurchaseButtonsPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}