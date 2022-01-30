package application

import application.puntainers.SheetNumberDisplayer
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.GlobalScope
import pungine.PunScene
import pungine.PunStage
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

class LevelEndScene(stage: PunStage, val gameState: GameState) : PunScene("levelEnd", stage, GlobalAccess.virtualSize.width.toDouble(), GlobalAccess.virtualSize.height.toDouble()) {
    override suspend fun sceneInit() {
        super.sceneInit()

        punImage("bg", oneRectangle(), resourcesVfs["UI/between.png"].readBitmap())

        Button("nextLevel", GlobalAccess.rectFromXD(Vector(488.0,328.0),304,80),
            resourcesVfs["UI/play_next_normal.png"].readBitmap(),
            resourcesVfs["UI/play_next_pushed.png"].readBitmap(),
            resourcesVfs["UI/play_next_hover.png"].readBitmap(),
        ).also {
            addPuntainer(it)
            it.clickFunction = {
                onPlayNextPressed()
            }
        }

        addPuntainer(
            SheetNumberDisplayer.create( "money",
                GlobalAccess.rectFromXD(Vector(872.0,308.0),192,36),
                Rectangle(0.0, 192.0, 0.0, 36.0),
                5,
                false,
                moneySign = true

            ).also {
                it.setValue(gameState.money)
            }
        )

        addPuntainer(
            SheetNumberDisplayer.create( "levelNo",
                GlobalAccess.rectFromXD(Vector(572.0,44.0),52,36),
                Rectangle(0.0, 52.0, 0.0, 36.0),
                2,
                false
            ).also {
                it.setValue(gameState.level)
            }
        )

        Button("buyVin", GlobalAccess.rectFromXD(Vector(940.0,86.0),128,72),
            resourcesVfs["UI/buy_normal.png"].readBitmap(),
            resourcesVfs["UI/buy_pushed.png"].readBitmap(),
            resourcesVfs["UI/buy_hover.png"].readBitmap(),
        ).also {
            addPuntainer(it)
            it.clickFunction = {
                if(gameState.buy(sugarToBuy = 1)) {
                    sfxPlayer.play("cash-register.mp3")
                    updateInfoAfter(money=gameState.money, vinegarCount=gameState.vinegar, sugarCount=gameState.sugar)
                    println("MONEY: ${gameState.money}, VINEGAR: ${gameState.vinegar}, SUGAR: ${gameState.sugar}")
                }
            }
        }

        addPuntainer(
            SheetNumberDisplayer.create( "vinPrice",
                GlobalAccess.rectFromXD(Vector(964.0,104.0),80,36),
                Rectangle(0.0, 80.0, 0.0, 36.0),
                2,
                false,
                moneySign = true
            ).also {
                it.setValue(20)
            }
        )

        Button("buySug", GlobalAccess.rectFromXD(Vector(940.0,158.0),128,72),
            resourcesVfs["UI/buy_normal.png"].readBitmap(),
            resourcesVfs["UI/buy_pushed.png"].readBitmap(),
            resourcesVfs["UI/buy_hover.png"].readBitmap(),
        ).also {
            addPuntainer(it)
            it.clickFunction = {
                if(gameState.buy(vinegarToBuy = 1)) {
                    sfxPlayer.play("cash-register.mp3")
                    updateInfoAfter(money=gameState.money, vinegarCount=gameState.vinegar, sugarCount=gameState.sugar)
                    println("MONEY: ${gameState.money}, VINEGAR: ${gameState.vinegar}, SUGAR: ${gameState.sugar}")
                }
            }
        }

        addPuntainer(
            SheetNumberDisplayer.create( "sugPrice",
                GlobalAccess.rectFromXD(Vector(964.0,176.0),80,36),
                Rectangle(0.0, 80.0, 0.0, 36.0),
                2,
                false,
                moneySign = true

            ).also {
                it.setValue(20)
            }
        )

        addPuntainer(
            SheetNumberDisplayer.create( "operationsCost",
                GlobalAccess.rectFromXD(Vector(900.0,240.0),164,36),
                Rectangle(0.0, 164.0, 0.0, 36.0),
                4,
                false,
                moneySign = true

            ).also {
                it.setValue(GlobalAccess.levels[gameState.level].rent)
            }
        )


        addPuntainer(
            punImage("vinBar",
                GlobalAccess.rectFromXD(Vector(432.0,180.0),480,24),
                resourcesVfs["UI/VinBar_inside.png"].readBitmap()
            )
        )

        addPuntainer(
            punImage("sugBar",
                GlobalAccess.rectFromXD(Vector(432.0,108.0),480,24),
                resourcesVfs["UI/SugBar_inside.png"].readBitmap()
            )
        )

        gameState.gameOver = {

        }

        gameState.payRent()
        updateInfoAfter()
    }

    fun updateInfoAfter(levelNo: Int = gameState.level, opCost: Int= GlobalAccess.levels[gameState.level].rent, money: Int = gameState.money, vinegarCount: Int = gameState.vinegar, sugarCount: Int = gameState.sugar){
        toPuntainer("levelNo", forceReshape = true){  it as SheetNumberDisplayer
            it.setValue(levelNo)
        }

        toPuntainer("operationsCost", forceReshape = true){  it as SheetNumberDisplayer
            it.setValue(opCost)
        }

        toPuntainer("money", forceReshape = true){  it as SheetNumberDisplayer
            it.setValue(money)
        }

        updateSugarCount(sugarCount)
        updateVinCount(vinegarCount)
    }

    fun onPlayNextPressed(){
        gameState.level++
        val newLevel = TestScene(stage, gameState)
        GlobalScope.launchImmediately {
            newLevel.initialize()
            stage.scenesToAdd.add(Pair(newLevel, true))
            stage.scenesToRemove.add("levelEnd")
        }
    }

    fun updateSugarCount(n: Int){
        toPuntainer("sugBar", forceReshape = true){
            it.resizeRect(GlobalAccess.rectFromXD(Vector(432.0,108.0),(480/10.0*n).toInt(),24))
        }
    }

    fun updateVinCount(n: Int) {
        toPuntainer("vinBar", forceReshape = true) {
            it.resizeRect(GlobalAccess.rectFromXD(Vector(432.0, 180.0), (480 / 10.0 * n).toInt(), 24))
        }
    }
}